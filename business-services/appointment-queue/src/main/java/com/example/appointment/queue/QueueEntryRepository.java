package com.example.appointment.queue;
import java.time.LocalDate;
import java.util.*;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {
  Optional<QueueEntry> findByAppointmentId(Long id);
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select q from QueueEntry q where q.appointment.staff.id=:s and q.status='WAITING' order "
      + "by q.joinedAt,q.id")
  List<QueueEntry>
  lockWaiting(@Param("s") Long staffId);
  @Query("select q from QueueEntry q where q.appointment.staff.id=:s and q.status='WAITING' order "
      + "by q.joinedAt,q.id")
  List<QueueEntry>
  waiting(@Param("s") Long staffId);
  long countByAppointmentAppointmentDate(LocalDate date);
}
