package com.example.expense.controller;

import com.example.expense.dto.*;
import com.example.expense.service.TransactionService;
import javax.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expense-transactions")
public class TransactionController {
  public static final String ACTOR_ATTRIBUTE = "expenseActor";
  private final TransactionService service;
  public TransactionController(TransactionService s) {
    service = s;
  }
  @PostMapping
  public ResponseEntity<TransactionResponse> create(
      @Valid @RequestBody TransactionRequest r, @RequestAttribute(ACTOR_ATTRIBUTE) ExpenseActor a) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r, a));
  }
  @GetMapping
  public Page<TransactionResponse> all(@ModelAttribute TransactionFilterRequest f, Pageable p,
      @RequestAttribute(ACTOR_ATTRIBUTE) ExpenseActor a) {
    return service.findAll(f, p, a);
  }
  @GetMapping("/{id}")
  public TransactionResponse one(
      @PathVariable Long id, @RequestAttribute(ACTOR_ATTRIBUTE) ExpenseActor a) {
    return service.findById(id, a);
  }
  @PutMapping("/{id}")
  public TransactionResponse update(@PathVariable Long id, @Valid @RequestBody TransactionRequest r,
      @RequestAttribute(ACTOR_ATTRIBUTE) ExpenseActor a) {
    return service.update(id, r, a);
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Long id, @RequestAttribute(ACTOR_ATTRIBUTE) ExpenseActor a) {
    service.delete(id, a);
    return ResponseEntity.noContent().build();
  }
}
