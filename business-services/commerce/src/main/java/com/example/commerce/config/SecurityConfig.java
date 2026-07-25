package com.example.commerce.config;
import com.example.commerce.user.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import org.springframework.web.filter.OncePerRequestFilter;
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  SecurityFilterChain chain(HttpSecurity h, JwtFilter f,
      @Value("${commerce.cors-origins}") String origins) throws Exception {
    return h.csrf()
        .disable()
        .cors()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/api/auth/**", "/api/products/**", "/api/categories/**", "/v3/api-docs/**",
            "/swagger-ui/**", "/actuator/health")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(f, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint((q, r, e) -> r.sendError(401, "Authentication required"))
        .and()
        .build();
  }
  @Bean
  CorsConfigurationSource cors(@Value("${commerce.cors-origins}") String origins) {
    CorsConfiguration c = new CorsConfiguration();
    c.setAllowedOrigins(Arrays.asList(origins.split(",")));
    c.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    c.setAllowedHeaders(
        Arrays.asList("Authorization", "Content-Type", "Idempotency-Key", "X-Correlation-Id"));
    UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
    s.registerCorsConfiguration("/**", c);
    return s;
  }
  @Bean
  JwtService jwtService(@Value("${commerce.jwt.secret}") String secret,
      @Value("${commerce.jwt.expiration-seconds}") long expiry) {
    if (secret == null || secret.length() < 43)
      throw new IllegalStateException(
          "JWT_SECRET must be a Base64 value encoding at least 32 bytes");
    return new JwtService(secret, expiry);
  }
  @Bean
  JwtFilter jwtFilter(JwtService jwt, UserRepository users) {
    return new JwtFilter(jwt, users);
  }
  public static User currentUser() {
    Authentication a = SecurityContextHolder.getContext().getAuthentication();
    if (a == null || !(a.getPrincipal() instanceof User))
      throw new BadCredentialsException("Authentication required");
    return (User) a.getPrincipal();
  }
  public static class JwtService {
    private final byte[] key;
    private final long expiry;
    JwtService(String s, long e) {
      key = Decoders.BASE64.decode(s);
      expiry = e;
    }
    public String issue(User u) {
      Date now = new Date();
      return Jwts.builder()
          .setSubject(u.getId().toString())
          .claim("role", u.getRole().name())
          .setIssuedAt(now)
          .setExpiration(new Date(now.getTime() + expiry * 1000))
          .signWith(Keys.hmacShaKeyFor(key))
          .compact();
    }
    Long parse(String t) {
      return Long.valueOf(Jwts.parserBuilder()
              .setSigningKey(Keys.hmacShaKeyFor(key))
              .build()
              .parseClaimsJws(t)
              .getBody()
              .getSubject());
    }
  }
  public static class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final UserRepository users;
    JwtFilter(JwtService j, UserRepository u) {
      jwt = j;
      users = u;
    }
    protected void doFilterInternal(HttpServletRequest q, HttpServletResponse r, FilterChain c)
        throws ServletException, IOException {
      String h = q.getHeader(HttpHeaders.AUTHORIZATION);
      if (h != null && h.startsWith("Bearer "))
        try {
          User u = users.findById(jwt.parse(h.substring(7))).filter(User::isActive).orElse(null);
          if (u != null)
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(u, null,
                    Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + u.getRole().name()))));
        } catch (JwtException | IllegalArgumentException ignored) {
          SecurityContextHolder.clearContext();
        }
      c.doFilter(q, r);
    }
  }
}
