package com.example.appointment.staff;
import com.example.appointment.common.DomainException;
import com.example.appointment.offering.*;
import com.example.appointment.user.*;
import java.time.*;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
public class StaffController {
  private final StaffProfileRepository staff;
  private final AvailabilityRepository availability;
  private final UserRepository users;
  private final ServiceOfferingRepository offerings;
  private final PasswordEncoder encoder;
  public StaffController(StaffProfileRepository s, AvailabilityRepository a, UserRepository u,
      ServiceOfferingRepository o, PasswordEncoder e) {
    staff = s;
    availability = a;
    users = u;
    offerings = o;
    encoder = e;
  }
  @PostMapping("/api/admin/staff")
  @ResponseStatus(HttpStatus.CREATED)
  StaffResponse create(@Valid @RequestBody StaffRequest r) {
    if (users.existsByEmailIgnoreCase(r.email))
      throw DomainException.conflict("DUPLICATE_EMAIL", "Email is already registered");
    User u = users.save(new User(r.name, r.email, encoder.encode(r.password), Role.STAFF));
    return out(staff.save(new StaffProfile(u, r.specialisation)));
  }
  @GetMapping("/api/admin/staff")
  List<StaffResponse> all() {
    List<StaffResponse> x = new ArrayList<>();
    staff.findAll().forEach(s -> x.add(out(s)));
    return x;
  }
  @GetMapping("/api/admin/staff/{id}")
  StaffResponse get(@PathVariable Long id) {
    return out(find(id));
  }
  @PutMapping("/api/admin/staff/{id}")
  StaffResponse update(@PathVariable Long id, @RequestBody StaffUpdate r) {
    StaffProfile s = find(id);
    s.update(r.specialisation, r.active);
    return out(staff.save(s));
  }
  @PostMapping("/api/admin/staff/{id}/services/{service}")
  StaffResponse assign(@PathVariable Long id, @PathVariable Long service) {
    StaffProfile s = find(id);
    s.getServices().add(offerings.findById(service).orElseThrow(
        () -> DomainException.notFound("Service not found")));
    return out(staff.save(s));
  }
  @DeleteMapping("/api/admin/staff/{id}/services/{service}")
  StaffResponse unassign(@PathVariable Long id, @PathVariable Long service) {
    StaffProfile s = find(id);
    s.getServices().removeIf(x -> x.getId().equals(service));
    return out(staff.save(s));
  }
  @PostMapping("/api/staff/{id}/availability")
  @PreAuthorize("hasRole('ADMIN') or @staffController.owns(#id,authentication)")
  @ResponseStatus(HttpStatus.CREATED)
  AvailabilityResponse add(@PathVariable Long id, @Valid @RequestBody AvailabilityRequest r,
      Authentication authentication) {
    validateOverlap(id, r, null);
    return av(availability.save(new Availability(find(id), r.date, r.startTime, r.endTime)));
  }
  @GetMapping("/api/staff/{id}/availability")
  @PreAuthorize("hasRole('ADMIN') or @staffController.owns(#id,authentication)")
  List<AvailabilityResponse> availability(@PathVariable Long id, Authentication authentication) {
    List<AvailabilityResponse> x = new ArrayList<>();
    availability.findByStaffIdOrderByDateAscStartTimeAsc(id).forEach(a -> x.add(av(a)));
    return x;
  }
  @PutMapping("/api/staff/{id}/availability/{aid}")
  @PreAuthorize("hasRole('ADMIN') or @staffController.owns(#id,authentication)")
  AvailabilityResponse change(@PathVariable Long id, @PathVariable Long aid,
      @Valid @RequestBody AvailabilityRequest r, Authentication authentication) {
    Availability a = availability.findById(aid)
                         .filter(x -> x.getStaff().getId().equals(id))
                         .orElseThrow(() -> DomainException.notFound("Availability not found"));
    validateOverlap(id, r, aid);
    a.update(r.date, r.startTime, r.endTime);
    return av(availability.save(a));
  }
  @DeleteMapping("/api/staff/{id}/availability/{aid}")
  @PreAuthorize("hasRole('ADMIN') or @staffController.owns(#id,authentication)")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void remove(@PathVariable Long id, @PathVariable Long aid, Authentication authentication) {
    Availability a = availability.findById(aid)
                         .filter(x -> x.getStaff().getId().equals(id))
                         .orElseThrow(() -> DomainException.notFound("Availability not found"));
    availability.delete(a);
  }
  public boolean owns(Long id, Authentication a) {
    return a != null && ((User) a.getPrincipal()).getRole() == Role.STAFF
        && staff.findByUserId(((User) a.getPrincipal()).getId())
               .map(x -> x.getId().equals(id))
               .orElse(false);
  }
  private void validateOverlap(Long id, AvailabilityRequest r, Long ignore) {
    if (!r.startTime.isBefore(r.endTime))
      throw DomainException.bad("INVALID_AVAILABILITY", "startTime must be before endTime");
    boolean overlap = availability.findByStaffIdAndDateOrderByStartTime(id, r.date)
                          .stream()
                          .anyMatch(a
                              -> !a.getId().equals(ignore) && a.getStartTime().isBefore(r.endTime)
                                  && a.getEndTime().isAfter(r.startTime));
    if (overlap)
      throw DomainException.conflict(
          "AVAILABILITY_OVERLAP", "Availability overlaps an existing record");
  }
  private StaffProfile find(Long id) {
    return staff.findById(id).orElseThrow(() -> DomainException.notFound("Staff not found"));
  }
  private StaffResponse out(StaffProfile s) {
    List<Long> ids = new ArrayList<>();
    s.getServices().forEach(x -> ids.add(x.getId()));
    return new StaffResponse(s.getId(), s.getUser().getName(), s.getUser().getEmail(),
        s.getSpecialisation(), s.isActive(), ids);
  }
  private AvailabilityResponse av(Availability a) {
    return new AvailabilityResponse(a.getId(), a.getDate(), a.getStartTime(), a.getEndTime());
  }
  public static class StaffRequest {
    @NotBlank public String name;
    @Email public String email;
    @Size(min = 8) public String password;
    public String specialisation;
  }
  public static class StaffUpdate {
    public String specialisation;
    public boolean active;
  }
  public static class AvailabilityRequest {
    @NotNull public LocalDate date;
    @NotNull public LocalTime startTime;
    @NotNull public LocalTime endTime;
  }
  public static class StaffResponse {
    public final Long id;
    public final String name, email, specialisation;
    public final boolean active;
    public final List<Long> serviceIds;
    StaffResponse(Long i, String n, String e, String s, boolean a, List<Long> x) {
      id = i;
      name = n;
      email = e;
      specialisation = s;
      active = a;
      serviceIds = x;
    }
  }
  public static class AvailabilityResponse {
    public final Long id;
    public final LocalDate date;
    public final LocalTime startTime, endTime;
    AvailabilityResponse(Long i, LocalDate d, LocalTime s, LocalTime e) {
      id = i;
      date = d;
      startTime = s;
      endTime = e;
    }
  }
}
