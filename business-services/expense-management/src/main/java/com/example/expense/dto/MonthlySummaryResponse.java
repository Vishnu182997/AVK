package com.example.expense.dto;
import java.math.BigDecimal; import java.util.*; import com.example.expense.model.ExpenseCategory; import lombok.Getter; import lombok.Setter;
@Getter @Setter public class MonthlySummaryResponse { private Integer month; private Integer year; private BigDecimal totalIncome; private BigDecimal totalExpenses; private BigDecimal netBalance; private Map<ExpenseCategory,BigDecimal> expensesByCategory; private List<CategorySummary> categories; }
