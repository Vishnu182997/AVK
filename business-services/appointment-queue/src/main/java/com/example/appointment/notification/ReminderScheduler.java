package com.example.appointment.notification;
import com.example.appointment.appointment.*;
import java.time.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
public class ReminderScheduler {
  private final AppointmentRepository repo;
  private final NotificationService notifications;
  private final int hours;
  public ReminderScheduler(AppointmentRepository r, NotificationService n,
      @Value("${app.reminder.window-hours:24}") int h) {
    repo = r;
    notifications = n;
    hours = h;
  }
  @Scheduled(fixedDelayString = "${app.reminder.scan-ms:300000}")
  @Transactional
  public void remind() {
    LocalDate today = LocalDate.now(), end = LocalDateTime.now().plusHours(hours).toLocalDate();
    for (Appointment a :
        repo.findByStatusAndAppointmentDateBetween(AppointmentStatus.BOOKED, today, end))
      if (!a.isReminderSent()
          && LocalDateTime.of(a.getAppointmentDate(), a.getStartTime())
              .isBefore(LocalDateTime.now().plusHours(hours))) {
        a.reminderSent();
        notifications.reminder(a);
      }
  }
}
