package com.example.expense.dto;

import com.example.expense.model.*;
import java.math.BigDecimal;
import java.time.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
  private Long id;
  private TransactionType type;
  private BigDecimal amount;
  private ExpenseCategory category;
  private String description;
  private LocalDate transactionDate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
