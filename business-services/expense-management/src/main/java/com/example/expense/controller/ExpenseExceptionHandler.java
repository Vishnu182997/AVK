package com.example.expense.controller;

import com.example.expense.exception.ExpenseException;
import java.util.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice(
    assignableTypes = {TransactionController.class, BudgetController.class, ReportController.class})
public class ExpenseExceptionHandler {
  @ExceptionHandler(ExpenseException.class)
  public ResponseEntity<Map<String, Object>> expense(ExpenseException e) {
    return ResponseEntity.status(e.getStatusCode()).body(error(e.getStatusCode(), e.getMessage()));
  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException e) {
    String m = e.getBindingResult().getFieldErrors().isEmpty()
        ? "Request validation failed"
        : e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    return ResponseEntity.badRequest().body(error(400, m));
  }
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<Map<String, Object>> data() {
    return ResponseEntity.status(500).body(error(500, "Unable to access expense data"));
  }
  private Map<String, Object> error(int s, String m) {
    Map<String, Object> x = new LinkedHashMap<>();
    x.put("status", s);
    x.put("message", m);
    return x;
  }
}
