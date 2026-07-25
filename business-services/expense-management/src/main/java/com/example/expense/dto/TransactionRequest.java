package com.example.expense.dto;
import java.math.BigDecimal; import java.time.LocalDate; import javax.validation.constraints.*; import com.example.expense.model.*; import lombok.Getter; import lombok.Setter;
@Getter @Setter public class TransactionRequest {
 @NotNull private TransactionType type;
 @NotNull @DecimalMin(value="0.01") private BigDecimal amount;
 @NotNull private ExpenseCategory category;
 @Size(max=500) private String description;
 @NotNull private LocalDate transactionDate;
}
