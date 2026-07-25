package com.example.expense.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

import com.example.expense.model.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Expense_Transaction",
        indexes =
        {
            @Index(name = "IX_Expense_Transaction_User_Date", columnList = "User_Id,Transaction_Date")
            , @Index(name = "IX_Expense_Transaction_User_Category", columnList = "User_Id,Category")
        })
public class ExpenseTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Transaction_Id")
    private Long id;
    @Column(name = "User_Id", nullable = false, length = 100)
    private String userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false, length = 10)
    private TransactionType type;
    @Column(name = "Amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "Category", nullable = false, length = 30)
    private ExpenseCategory category;
    @Column(name = "Description", length = 500)
    private String description;
    @Column(name = "Transaction_Date", nullable = false)
    private LocalDate transactionDate;
    @Column(name = "Created_At", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "Updated_At", nullable = false)
    private LocalDateTime updatedAt;
}
