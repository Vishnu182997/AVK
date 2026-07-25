package com.example.commerce.user;
import javax.validation.constraints.*;
import lombok.*;
public final class AuthDtos {
  private AuthDtos() {}
  @Getter
  @Setter
  public static class RegisterRequest {
    @NotBlank @Size(max = 100) private String name;
    @NotBlank @Email @Size(max = 254) private String email;
    @NotBlank @Size(min = 10, max = 72) private String password;
  }
  @Getter
  @Setter
  public static class LoginRequest {
    @NotBlank @Email private String email;
    @NotBlank private String password;
  }
  @Getter
  @AllArgsConstructor
  public static class AuthResponse {
    private final String accessToken;
    private final String tokenType;
    private final long userId;
    private final String email;
    private final User.Role role;
  }
  @Getter
  @AllArgsConstructor
  public static class UserResponse {
    private final long id;
    private final String name;
    private final String email;
    private final User.Role role;
  }
}
