package com.example.appointment.user;
import com.example.appointment.common.DomainException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final String secret;
  private final long expiry;
  public AuthController(UserRepository u, PasswordEncoder e, @Value("${app.jwt.secret}") String s,
      @Value("${app.jwt.expiry-seconds:3600}") long x) {
    users = u;
    encoder = e;
    secret = s;
    expiry = x;
  }
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse register(@Valid @RequestBody RegisterRequest r) {
    if (users.existsByEmailIgnoreCase(r.email))
      throw DomainException.conflict("DUPLICATE_EMAIL", "Email is already registered");
    return out(users.save(new User(r.name, r.email, encoder.encode(r.password), Role.CUSTOMER)));
  }
  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody LoginRequest r) {
    User u = users.findByEmailIgnoreCase(r.email).orElseThrow(
        () -> DomainException.bad("INVALID_CREDENTIALS", "Invalid credentials"));
    if (!encoder.matches(r.password, u.getPassword()))
      throw DomainException.bad("INVALID_CREDENTIALS", "Invalid credentials");
    Instant now = Instant.now();
    String t = Jwts.builder()
                   .setSubject(String.valueOf(u.getId()))
                   .claim("role", u.getRole().name())
                   .setIssuedAt(Date.from(now))
                   .setExpiration(Date.from(now.plusSeconds(expiry)))
                   .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                   .compact();
    return new AuthResponse(t, "Bearer", expiry, out(u));
  }
  private UserResponse out(User u) {
    return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole());
  }
  public static class RegisterRequest {
    @NotBlank public String name;
    @Email @NotBlank public String email;
    @Size(min = 8, max = 100) public String password;
  }
  public static class LoginRequest {
    @Email @NotBlank public String email;
    @NotBlank public String password;
  }
  public static class UserResponse {
    public final Long id;
    public final String name, email;
    public final Role role;
    UserResponse(Long i, String n, String e, Role r) {
      id = i;
      name = n;
      email = e;
      role = r;
    }
  }
  public static class AuthResponse {
    public final String accessToken, tokenType;
    public final long expiresIn;
    public final UserResponse user;
    AuthResponse(String a, String t, long e, UserResponse u) {
      accessToken = a;
      tokenType = t;
      expiresIn = e;
      user = u;
    }
  }
}
