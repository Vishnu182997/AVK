package com.example.expense.service;

import com.example.expense.dto.*;
import java.util.List;

public interface BudgetService {
  BudgetResponse create(BudgetRequest request, ExpenseActor actor);
  BudgetResponse findById(Long id, ExpenseActor actor);
  List<BudgetResponse> findAll(Integer month, Integer year, ExpenseActor actor);
  BudgetResponse update(Long id, BudgetRequest request, ExpenseActor actor);
  void delete(Long id, ExpenseActor actor);
}
