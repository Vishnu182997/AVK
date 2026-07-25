package com.example.expense.mapper;

import com.example.expense.dto.*;
import com.example.expense.entity.*;

public final class ExpenseMapper {
  private ExpenseMapper() {}
  public static TransactionResponse toResponse(ExpenseTransaction t) {
    TransactionResponse r = new TransactionResponse();
    r.setId(t.getId());
    r.setType(t.getType());
    r.setAmount(t.getAmount());
    r.setCategory(t.getCategory());
    r.setDescription(t.getDescription());
    r.setTransactionDate(t.getTransactionDate());
    r.setCreatedAt(t.getCreatedAt());
    r.setUpdatedAt(t.getUpdatedAt());
    return r;
  }
  public static BudgetResponse toResponse(Budget b) {
    BudgetResponse r = new BudgetResponse();
    r.setId(b.getId());
    r.setCategory(b.getCategory());
    r.setMonthlyLimit(b.getMonthlyLimit());
    r.setMonth(b.getMonth());
    r.setYear(b.getYear());
    r.setCreatedAt(b.getCreatedAt());
    r.setUpdatedAt(b.getUpdatedAt());
    return r;
  }
}
