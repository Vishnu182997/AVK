package com.example.appointment.common;
import java.time.Instant;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ApiErrorHandler {
  @ExceptionHandler(DomainException.class)
  ResponseEntity<Map<String, Object>> domain(DomainException e, HttpServletRequest r) {
    return body(
        e.getStatus(), e.getCode(), e.getMessage(), r.getRequestURI(), Collections.emptyList());
  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Map<String, Object>> validation(
      MethodArgumentNotValidException e, HttpServletRequest r) {
    List<String> f = new ArrayList<>();
    e.getBindingResult().getFieldErrors().forEach(
        x -> f.add(x.getField() + ": " + x.getDefaultMessage()));
    return body(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request validation failed",
        r.getRequestURI(), f);
  }
  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<Map<String, Object>> constraint(Exception e, HttpServletRequest r) {
    return body(HttpStatus.CONFLICT, "CONSTRAINT_VIOLATION",
        "The requested operation conflicts with existing data", r.getRequestURI(),
        Collections.emptyList());
  }
  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<Map<String, Object>> denied(Exception e, HttpServletRequest r) {
    return body(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "Access denied", r.getRequestURI(),
        Collections.emptyList());
  }
  private ResponseEntity<Map<String, Object>> body(
      HttpStatus s, String c, String m, String p, List<String> f) {
    Map<String, Object> b = new LinkedHashMap<>();
    b.put("timestamp", Instant.now());
    b.put("status", s.value());
    b.put("error", s.getReasonPhrase());
    b.put("code", c);
    b.put("message", m);
    b.put("path", p);
    b.put("fieldErrors", f);
    return ResponseEntity.status(s).body(b);
  }
}
