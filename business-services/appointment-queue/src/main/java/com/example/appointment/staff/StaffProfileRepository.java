package com.example.appointment.staff;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface StaffProfileRepository extends JpaRepository<StaffProfile, Long> {
  Optional<StaffProfile> findByUserId(Long id);
  @Query("select distinct s from StaffProfile s join s.services o where s.active=true and "
         + "o.id=:serviceId")
  List<StaffProfile>
  activeForService(@Param("serviceId") Long id);
}
