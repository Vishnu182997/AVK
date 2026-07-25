package com.example.appointment.waitlist;
import com.example.appointment.appointment.Appointment;
import com.example.appointment.common.BaseEntity;
import com.example.appointment.offering.ServiceOffering;
import com.example.appointment.staff.StaffProfile;
import com.example.appointment.user.User;
import java.time.*;
import javax.persistence.*;
@Entity
@Table(name = "waitlist_entry")
public class WaitlistEntry extends BaseEntity {
  public enum Status { WAITING, OFFERED, ACCEPTED, EXPIRED, CANCELLED }
  @ManyToOne(optional = false) private User customer;
  @ManyToOne(optional = false) private ServiceOffering service;
  @ManyToOne private StaffProfile preferredStaff;
  private LocalDate preferredDate;
  private LocalTime preferredStartTime, preferredEndTime;
  @Enumerated(EnumType.STRING) private Status status = Status.WAITING;
  @OneToOne private Appointment offeredAppointment;
  private Instant offerExpiresAt;
  @Version private long version;
  protected WaitlistEntry() {}
  public WaitlistEntry(
      User u, ServiceOffering s, StaffProfile st, LocalDate d, LocalTime a, LocalTime b) {
    customer = u;
    service = s;
    preferredStaff = st;
    preferredDate = d;
    preferredStartTime = a;
    preferredEndTime = b;
  }
  public User getCustomer() {
    return customer;
  }
  public ServiceOffering getService() {
    return service;
  }
  public StaffProfile getPreferredStaff() {
    return preferredStaff;
  }
  public LocalDate getPreferredDate() {
    return preferredDate;
  }
  public LocalTime getPreferredStartTime() {
    return preferredStartTime;
  }
  public LocalTime getPreferredEndTime() {
    return preferredEndTime;
  }
  public Status getStatus() {
    return status;
  }
  public Instant getOfferExpiresAt() {
    return offerExpiresAt;
  }
  public Appointment getOfferedAppointment() {
    return offeredAppointment;
  }
  public void offer(Appointment a, Instant e) {
    status = Status.OFFERED;
    offeredAppointment = a;
    offerExpiresAt = e;
  }
  public void accept() {
    status = Status.ACCEPTED;
  }
  public void expire() {
    status = Status.EXPIRED;
  }
  public void cancel() {
    status = Status.CANCELLED;
  }
}
