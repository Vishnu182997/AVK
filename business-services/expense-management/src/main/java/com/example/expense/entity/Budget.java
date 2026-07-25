package com.example.expense.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;

import com.example.expense.model.ExpenseCategory;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Expense_Budget",
        uniqueConstraints = @UniqueConstraint(name = "UK_Expense_Budget_User_Category_Month",
                columnNames = {"User_Id", "Category", "Budget_Month", "Budget_Year"}),
        indexes = @Index(name = "IX_Expense_Budget_User_Month", columnList = "User_Id,Budget_Year,Budget_Month"))
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Budget_Id")
    private Long id;
    @Column(name = "User_Id", nullable = false, length = 100)
    private String userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "Category", nullable = false, length = 30)
    private ExpenseCategory category;
    @Column(name = "Monthly_Limit", nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyLimit;
    @Column(name = "Budget_Month", nullable = false)
    private Integer month;
    @Column(name = "Budget_Year", nullable = false)
    private Integer year;
    @Column(name = "Created_At", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "Updated_At", nullable = false)
    private LocalDateTime updatedAt;
}
