package com.example.expense.repository;

import com.example.expense.entity.Budget;
import com.example.expense.model.ExpenseCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
  List<Budget> findByUserIdAndMonthAndYear(String userId, Integer month, Integer year);
  boolean existsByUserIdAndCategoryAndMonthAndYear(
      String userId, ExpenseCategory category, Integer month, Integer year);
  boolean existsByUserIdAndCategoryAndMonthAndYearAndIdNot(
      String userId, ExpenseCategory category, Integer month, Integer year, Long id);
}
