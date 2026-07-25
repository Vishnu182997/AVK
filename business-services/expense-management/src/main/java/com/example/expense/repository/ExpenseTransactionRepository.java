package com.example.expense.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.expense.entity.ExpenseTransaction;

public interface ExpenseTransactionRepository
        extends JpaRepository<ExpenseTransaction, Long>, JpaSpecificationExecutor<ExpenseTransaction> {
    @Query("select t from ExpenseTransaction t where t.userId=:userId and t.transactionDate between :from and :to")
    List<ExpenseTransaction> findMonthly(
            @Param("userId") String userId, @Param("from") LocalDate from, @Param("to") LocalDate to);
}
