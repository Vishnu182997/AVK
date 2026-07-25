package com.example.appointment;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.example.appointment.appointment.*;
import com.example.appointment.common.DomainException;
import com.example.appointment.offering.ServiceOffering;
import com.example.appointment.staff.*;
import com.example.appointment.user.User;
import java.time.*;
import org.junit.jupiter.api.Test;
class DomainModelTest {
  @Test
  void availabilityRejectsInvalidRange() {
    assertThrows(IllegalArgumentException.class,
        ()
            -> new Availability(
                mock(StaffProfile.class), LocalDate.now(), LocalTime.NOON, LocalTime.NOON));
  }
  @Test
  void appointmentEnforcesLifecycle() {
    Appointment a = new Appointment("ABC", mock(User.class), mock(StaffProfile.class),
        new ServiceOffering("Consult", 30, ""), LocalDate.now().plusDays(1), LocalTime.NOON,
        LocalTime.NOON.plusMinutes(30));
    assertThrows(DomainException.class, a::complete);
    a.checkIn("A-001");
    a.start();
    a.complete();
    assertEquals(AppointmentStatus.COMPLETED, a.getStatus());
  }
  @Test
  void duplicateCheckInIsRejected() {
    Appointment a = new Appointment("ABC", mock(User.class), mock(StaffProfile.class),
        mock(ServiceOffering.class), LocalDate.now().plusDays(1), LocalTime.NOON,
        LocalTime.NOON.plusMinutes(30));
    a.checkIn("A-001");
    assertThrows(DomainException.class, () -> a.checkIn("A-002"));
  }
}
