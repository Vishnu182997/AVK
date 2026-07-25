package com.example.expense.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.expense.dto.*;
import com.example.expense.service.BudgetService;

@RestController
@RequestMapping("/api/expense-budgets")
public class BudgetController {
    private final BudgetService service;
    public BudgetController(BudgetService s) {
        service = s;
    }
    @PostMapping
    public ResponseEntity<BudgetResponse> create(@Valid @RequestBody BudgetRequest r,
            @RequestAttribute(TransactionController.ACTOR_ATTRIBUTE) ExpenseActor a) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r, a));
    }
    @GetMapping
    public List<BudgetResponse> all(@RequestParam Integer month, @RequestParam Integer year,
            @RequestAttribute(TransactionController.ACTOR_ATTRIBUTE) ExpenseActor a) {
        return service.findAll(month, year, a);
    }
    @GetMapping("/{id}")
    public BudgetResponse one(
            @PathVariable Long id, @RequestAttribute(TransactionController.ACTOR_ATTRIBUTE) ExpenseActor a) {
        return service.findById(id, a);
    }
    @PutMapping("/{id}")
    public BudgetResponse update(@PathVariable Long id, @Valid @RequestBody BudgetRequest r,
            @RequestAttribute(TransactionController.ACTOR_ATTRIBUTE) ExpenseActor a) {
        return service.update(id, r, a);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id, @RequestAttribute(TransactionController.ACTOR_ATTRIBUTE) ExpenseActor a) {
        service.delete(id, a);
        return ResponseEntity.noContent().build();
    }
}
