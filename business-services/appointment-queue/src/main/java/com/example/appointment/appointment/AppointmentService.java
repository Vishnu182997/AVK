package com.example.appointment.appointment;
import com.example.appointment.common.DomainException;
import com.example.appointment.notification.NotificationService;
import com.example.appointment.offering.*;
import com.example.appointment.queue.*;
import com.example.appointment.staff.*;
import com.example.appointment.user.*;
import com.example.appointment.waitlist.WaitlistService;
import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AppointmentService {
  private final AppointmentRepository appointments;
  private final ServiceOfferingRepository offerings;
  private final StaffProfileRepository staff;
  private final AvailabilityRepository availability;
  private final QueueEntryRepository queues;
  private final TokenSequenceRepository tokens;
  private final NotificationService notifications;
  private final QueuePublisher publisher;
  private final int checkInMinutes, cancelMinutes;
  private WaitlistService waitlist;
  public AppointmentService(AppointmentRepository a, ServiceOfferingRepository o,
      StaffProfileRepository s, AvailabilityRepository v, QueueEntryRepository q,
      TokenSequenceRepository t, NotificationService n, QueuePublisher p,
      @Value("${app.check-in-window-minutes:60}") int ci,
      @Value("${app.cancellation-window-minutes:60}") int ca) {
    appointments = a;
    offerings = o;
    staff = s;
    availability = v;
    queues = q;
    tokens = t;
    notifications = n;
    publisher = p;
    checkInMinutes = ci;
    cancelMinutes = ca;
  }
  public void setWaitlist(WaitlistService w) {
    waitlist = w;
  }
  @Transactional
  public Appointment book(
      User customer, Long serviceId, Long staffId, LocalDate date, LocalTime start) {
    ServiceOffering service =
        offerings.findById(serviceId)
            .filter(ServiceOffering::isActive)
            .orElseThrow(() -> DomainException.notFound("Active service not found"));
    StaffProfile member =
        appointments.lockStaff(staffId)
            .filter(StaffProfile::isActive)
            .orElseThrow(() -> DomainException.notFound("Active staff not found"));
    if (member.getServices().stream().noneMatch(x -> x.getId().equals(serviceId)))
      throw DomainException.bad("STAFF_SERVICE_MISMATCH", "Staff is not assigned to this service");
    LocalTime end = start.plusMinutes(service.getDurationMinutes());
    if (!LocalDateTime.of(date, start).isAfter(LocalDateTime.now()))
      throw DomainException.bad("PAST_APPOINTMENT", "Appointment must be in the future");
    boolean covered =
        availability.findByStaffIdAndDateOrderByStartTime(staffId, date)
            .stream()
            .anyMatch(a -> !start.isBefore(a.getStartTime()) && !end.isAfter(a.getEndTime()));
    if (!covered)
      throw DomainException.bad("OUTSIDE_AVAILABILITY", "Slot is outside staff availability");
    if (!appointments.overlaps(staffId, date, start, end).isEmpty())
      throw DomainException.conflict("SLOT_ALREADY_BOOKED", "Selected slot is unavailable");
    Appointment a = appointments.save(new Appointment(
        UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase(), customer,
        member, service, date, start, end));
    notifications.booking(a);
    return a;
  }
  @Transactional
  public Appointment cancel(Long id, User actor) {
    Appointment a = authorized(id, actor, false);
    if (LocalDateTime.of(a.getAppointmentDate(), a.getStartTime())
            .minusMinutes(cancelMinutes)
            .isBefore(LocalDateTime.now())
        && actor.getRole() != Role.ADMIN)
      throw DomainException.conflict(
          "CANCELLATION_WINDOW_CLOSED", "Cancellation window has closed");
    a.cancel();
    queues.findByAppointmentId(id).ifPresent(q -> {
      q.cancel();
      queues.save(q);
      publisher.update(q);
    });
    notifications.cancellation(a);
    if (waitlist != null)
      waitlist.offerFor(a);
    return a;
  }
  @Transactional
  public Appointment reschedule(
      Long id, User actor, Long staffId, LocalDate date, LocalTime start) {
    Appointment old = authorized(id, actor, false);
    ServiceOffering service = old.getService();
    StaffProfile member = appointments.lockStaff(staffId).orElseThrow(
        () -> DomainException.notFound("Staff not found"));
    LocalTime end = start.plusMinutes(service.getDurationMinutes());
    if (appointments.overlaps(staffId, date, start, end)
            .stream()
            .anyMatch(x -> !x.getId().equals(id)))
      throw DomainException.conflict("SLOT_ALREADY_BOOKED", "Selected slot is unavailable");
    boolean covered =
        availability.findByStaffIdAndDateOrderByStartTime(staffId, date)
            .stream()
            .anyMatch(a -> !start.isBefore(a.getStartTime()) && !end.isAfter(a.getEndTime()));
    if (!covered || member.getServices().stream().noneMatch(s -> s.getId().equals(service.getId())))
      throw DomainException.bad("INVALID_SLOT", "New slot is unavailable");
    old.reschedule(member, date, start, end);
    notifications.rescheduled(old);
    return old;
  }
  @Transactional
  public QueueEntry checkIn(Long id, User actor) {
    Appointment a = authorized(id, actor, true);
    LocalDateTime at = LocalDateTime.of(a.getAppointmentDate(), a.getStartTime());
    if (LocalDateTime.now().isBefore(at.minusMinutes(checkInMinutes))
        || LocalDateTime.now().isAfter(at.plusMinutes(checkInMinutes)))
      throw DomainException.conflict(
          "CHECK_IN_WINDOW_CLOSED", "Appointment is outside the check-in window");
    TokenSequence seq =
        tokens.findByDateAndServiceId(a.getAppointmentDate(), a.getService().getId())
            .orElseGet(()
                           -> tokens.save(
                               new TokenSequence(a.getAppointmentDate(), a.getService().getId())));
    String token = String.valueOf((char) ('A' + (a.getService().getId().intValue() - 1) % 26)) + "-"
        + String.format("%03d", seq.take());
    tokens.save(seq);
    a.checkIn(token);
    QueueEntry q = queues.save(new QueueEntry(a, token));
    recalculate(a.getStaff().getId());
    publisher.update(q);
    return q;
  }
  @Transactional
  public Appointment complete(Long id, User actor) {
    Appointment a = authorized(id, actor, true);
    if (actor.getRole() == Role.CUSTOMER)
      throw new org.springframework.security.access.AccessDeniedException(
          "Staff or admin access required");
    a.complete();
    QueueEntry q = queues.findByAppointmentId(id).orElseThrow(
        () -> DomainException.notFound("Queue entry not found"));
    q.complete();
    queues.save(q);
    recalculate(a.getStaff().getId());
    publisher.update(q);
    return a;
  }
  public Appointment authorized(Long id, User actor, boolean staffAllowed) {
    Appointment a = appointments.findById(id).orElseThrow(
        () -> DomainException.notFound("Appointment not found"));
    boolean own = a.getCustomer().getId().equals(actor.getId()),
            admin = actor.getRole() == Role.ADMIN,
            assigned = staffAllowed && actor.getRole() == Role.STAFF
        && a.getStaff().getUser().getId().equals(actor.getId());
    if (!own && !admin && !assigned)
      throw new org.springframework.security.access.AccessDeniedException("Access denied");
    return a;
  }
  public void recalculate(Long staffId) {
    List<QueueEntry> q = queues.waiting(staffId);
    int wait = 0;
    for (int i = 0; i < q.size(); i++) {
      QueueEntry e = q.get(i);
      e.position(i + 1, wait);
      wait += e.getAppointment().getService().getDurationMinutes();
    }
    queues.saveAll(q);
  }
  public Page<Appointment> list(User u, Pageable p) {
    return u.getRole() == Role.ADMIN ? appointments.findAll(p)
                                     : appointments.findByCustomerId(u.getId(), p);
  }
}
