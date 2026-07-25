package com.example.expense.dto;

import com.example.expense.model.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionFilterRequest {
  private LocalDate from;
  private LocalDate to;
  private ExpenseCategory category;
  private TransactionType type;
}
