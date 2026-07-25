package com.example.appointment.notification;
import com.example.appointment.appointment.Appointment;
import com.example.appointment.waitlist.WaitlistEntry;
import org.slf4j.*;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
public class EmailNotificationService implements NotificationService {
  private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);
  private final JavaMailSender mail;
  public EmailNotificationService(JavaMailSender m) {
    mail = m;
  }
  @Async
  public void booking(Appointment a) {
    send(a, "Booking confirmed: " + a.getConfirmationNumber());
  }
  @Async
  public void cancellation(Appointment a) {
    send(a, "Appointment cancelled: " + a.getConfirmationNumber());
  }
  @Async
  public void rescheduled(Appointment a) {
    send(a, "Appointment rescheduled: " + a.getConfirmationNumber());
  }
  @Async
  public void reminder(Appointment a) {
    send(a, "Appointment reminder: " + a.getConfirmationNumber());
  }
  @Async
  public void waitlistOffer(WaitlistEntry w) {
    try {
      SimpleMailMessage m = new SimpleMailMessage();
      m.setTo(w.getCustomer().getEmail());
      m.setSubject("Appointment slot available");
      m.setText("A requested appointment slot is available until " + w.getOfferExpiresAt());
      mail.send(m);
    } catch (RuntimeException e) {
      log.warn("Waitlist notification delivery failed for entry {}", w.getId());
    }
  }
  private void send(Appointment a, String text) {
    try {
      SimpleMailMessage m = new SimpleMailMessage();
      m.setTo(a.getCustomer().getEmail());
      m.setSubject("Appointment update");
      m.setText(text);
      mail.send(m);
    } catch (RuntimeException e) {
      log.warn("Appointment notification delivery failed for appointment {}", a.getId());
    }
  }
}
