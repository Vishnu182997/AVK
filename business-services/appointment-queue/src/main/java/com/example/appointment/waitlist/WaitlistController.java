package com.example.appointment.waitlist;
import com.example.appointment.appointment.Appointment;
import com.example.appointment.user.User;
import java.time.*;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/waitlist")
public class WaitlistController {
  private final WaitlistService service;
  public WaitlistController(WaitlistService s) {
    service = s;
  }
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Response join(@Valid @RequestBody Request r, Authentication a) {
    return out(service.join(user(a), r.serviceId, r.preferredStaffId, r.preferredDate,
        r.preferredStartTime, r.preferredEndTime));
  }
  @GetMapping
  List<Response> list(Authentication a) {
    List<Response> x = new ArrayList<>();
    service.list(user(a)).forEach(w -> x.add(out(w)));
    return x;
  }
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void cancel(@PathVariable Long id, Authentication a) {
    service.cancel(id, user(a));
  }
  @PostMapping("/{id}/accept")
  Map<String, Object> accept(@PathVariable Long id, Authentication a) {
    Appointment p = service.accept(id, user(a));
    return Collections.<String, Object>singletonMap("appointmentId", p.getId());
  }
  private User user(Authentication a) {
    return (User) a.getPrincipal();
  }
  private Response out(WaitlistEntry w) {
    return new Response(w.getId(), w.getService().getId(), w.getPreferredDate(), w.getStatus(),
        w.getOfferExpiresAt());
  }
  public static class Request {
    @NotNull public Long serviceId;
    public Long preferredStaffId;
    @NotNull public LocalDate preferredDate;
    @NotNull public LocalTime preferredStartTime, preferredEndTime;
  }
  public static class Response {
    public final Long id, serviceId;
    public final LocalDate preferredDate;
    public final WaitlistEntry.Status status;
    public final Instant offerExpiresAt;
    Response(Long i, Long s, LocalDate d, WaitlistEntry.Status x, Instant e) {
      id = i;
      serviceId = s;
      preferredDate = d;
      status = x;
      offerExpiresAt = e;
    }
  }
}
