package com.example.expense.dto;

import com.example.expense.model.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySummary {
  private ExpenseCategory category;
  private BigDecimal configuredBudget;
  private BigDecimal actualSpending;
  private BigDecimal remainingBudget;
  private BigDecimal percentageUsed;
  private BudgetStatus status;
}
