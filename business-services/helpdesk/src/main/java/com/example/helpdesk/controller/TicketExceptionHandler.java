package com.example.helpdesk.controller;

import com.example.helpdesk.exception.TicketException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = TicketController.class)
public class TicketExceptionHandler {
  @ExceptionHandler(TicketException.class)
  public ResponseEntity<Map<String, Object>> handleTicketException(TicketException exception) {
    return ResponseEntity.status(exception.getStatusCode())
        .body(error(exception.getStatusCode(), exception.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(
      MethodArgumentNotValidException exception) {
    String message = exception.getBindingResult().getFieldErrors().isEmpty()
        ? "Request validation failed"
        : exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    return ResponseEntity.badRequest().body(error(400, message));
  }

  private Map<String, Object> error(int status, String message) {
    Map<String, Object> response = new LinkedHashMap<String, Object>();
    response.put("status", status);
    response.put("message", message);
    return response;
  }
}
