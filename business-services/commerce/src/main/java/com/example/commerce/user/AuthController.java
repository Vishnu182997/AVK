package com.example.commerce.user;
import com.example.commerce.config.SecurityConfig;
import javax.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService service;
  public AuthController(AuthService s) {
    service = s;
  }
  @PostMapping("/register")
  ResponseEntity<AuthDtos.AuthResponse> register(@Valid @RequestBody AuthDtos.RegisterRequest r) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.register(r));
  }
  @PostMapping("/login")
  AuthDtos.AuthResponse login(@Valid @RequestBody AuthDtos.LoginRequest r) {
    return service.login(r);
  }
  @GetMapping("/me")
  AuthDtos.UserResponse me() {
    User u = SecurityConfig.currentUser();
    return new AuthDtos.UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole());
  }
}
