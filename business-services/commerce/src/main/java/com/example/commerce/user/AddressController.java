package com.example.commerce.user;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import com.example.commerce.config.SecurityConfig;
import lombok.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/addresses")
@PreAuthorize("hasRole('CUSTOMER')")
@Transactional
public class AddressController {
  private final AddressRepository repo;
  public AddressController(AddressRepository r) {
    repo = r;
  }
  @Getter
  @Setter
  public static class Request {
    @NotBlank @Size(max = 120) String recipient;
    @NotBlank @Size(max = 200) String line1;
    @Size(max = 200) String line2;
    @NotBlank @Size(max = 100) String city;
    @NotBlank @Size(max = 30) String postalCode;
    @NotBlank @Pattern(regexp = "[A-Za-z]{2}") String countryCode;
  }
  @Getter
  @AllArgsConstructor
  public static class Response {
    long id;
    String recipient;
    String line1;
    String line2;
    String city;
    String postalCode;
    String countryCode;
  }
  @PostMapping
  ResponseEntity<Response> create(@Valid @RequestBody Request r) {
    Address a = new Address();
    a.setUser(SecurityConfig.currentUser());
    a.setRecipient(r.getRecipient());
    a.setLine1(r.getLine1());
    a.setLine2(r.getLine2());
    a.setCity(r.getCity());
    a.setPostalCode(r.getPostalCode());
    a.setCountryCode(r.getCountryCode().toUpperCase());
    a = repo.save(a);
    return ResponseEntity.status(201).body(map(a));
  }
  private Response map(Address a) {
    return new Response(a.getId(), a.getRecipient(), a.getLine1(), a.getLine2(), a.getCity(),
        a.getPostalCode(), a.getCountryCode());
  }
}
