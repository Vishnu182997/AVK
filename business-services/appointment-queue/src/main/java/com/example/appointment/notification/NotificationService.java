package com.example.appointment.notification;
import com.example.appointment.appointment.Appointment;
import com.example.appointment.waitlist.WaitlistEntry;
public interface NotificationService {
  void booking(Appointment a);
  void cancellation(Appointment a);
  void rescheduled(Appointment a);
  void reminder(Appointment a);
  void waitlistOffer(WaitlistEntry w);
}
