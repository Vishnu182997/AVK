package com.example.expense.dto;

import java.math.BigDecimal;
import javax.validation.constraints.*;

import com.example.expense.model.ExpenseCategory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetRequest {
    @NotNull
    private ExpenseCategory category;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal monthlyLimit;
    @NotNull
    @Min(1)
    @Max(12)
    private Integer month;
    @NotNull
    @Min(1900)
    @Max(9999)
    private Integer year;
}
