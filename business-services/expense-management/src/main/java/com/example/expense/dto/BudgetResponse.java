package com.example.expense.dto;

import com.example.expense.model.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetResponse {
  private Long id;
  private ExpenseCategory category;
  private BigDecimal monthlyLimit;
  private Integer month;
  private Integer year;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
