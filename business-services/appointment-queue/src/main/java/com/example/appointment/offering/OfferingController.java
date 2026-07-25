package com.example.appointment.offering;
import com.example.appointment.common.DomainException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
public class OfferingController {
  private final ServiceOfferingRepository repo;
  public OfferingController(ServiceOfferingRepository r) {
    repo = r;
  }
  @GetMapping("/api/services")
  List<ServiceOfferingResponse> list() {
    List<ServiceOfferingResponse> x = new ArrayList<>();
    repo.findByActiveTrueOrderByNameAsc().forEach(s -> x.add(out(s)));
    return x;
  }
  @GetMapping("/api/services/{id}")
  ServiceOfferingResponse get(@PathVariable Long id) {
    ServiceOffering s = repo.findById(id)
                            .filter(ServiceOffering::isActive)
                            .orElseThrow(() -> DomainException.notFound("Service not found"));
    return out(s);
  }
  @PostMapping("/api/admin/services")
  @ResponseStatus(HttpStatus.CREATED)
  ServiceOfferingResponse create(@Valid @RequestBody Request r) {
    return out(repo.save(new ServiceOffering(r.name, r.durationMinutes, r.description)));
  }
  @PutMapping("/api/admin/services/{id}")
  ServiceOfferingResponse update(@PathVariable Long id, @Valid @RequestBody Request r) {
    ServiceOffering s = find(id);
    s.update(r.name, r.durationMinutes, r.description);
    return out(repo.save(s));
  }
  @PatchMapping("/api/admin/services/{id}/status")
  ServiceOfferingResponse status(@PathVariable Long id, @RequestBody Status r) {
    ServiceOffering s = find(id);
    s.setActive(r.active);
    return out(repo.save(s));
  }
  @DeleteMapping("/api/admin/services/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@PathVariable Long id) {
    ServiceOffering s = find(id);
    s.setActive(false);
    repo.save(s);
  }
  private ServiceOffering find(Long id) {
    return repo.findById(id).orElseThrow(() -> DomainException.notFound("Service not found"));
  }
  public static ServiceOfferingResponse out(ServiceOffering s) {
    return new ServiceOfferingResponse(
        s.getId(), s.getName(), s.getDurationMinutes(), s.getDescription(), s.isActive());
  }
  public static class Request {
    @NotBlank public String name;
    @Min(1) @Max(1440) public int durationMinutes;
    public String description;
  }
  public static class Status {
    public boolean active;
  }
  public static class ServiceOfferingResponse {
    public final Long id;
    public final String name;
    public final int durationMinutes;
    public final String description;
    public final boolean active;
    ServiceOfferingResponse(Long i, String n, int d, String x, boolean a) {
      id = i;
      name = n;
      durationMinutes = d;
      description = x;
      active = a;
    }
  }
}
