package com.example.expense.service.impl;

import java.math.*;
import java.time.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expense.dto.*;
import com.example.expense.entity.*;
import com.example.expense.model.*;
import com.example.expense.repository.*;
import com.example.expense.service.ReportService;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {
    private final ExpenseTransactionRepository transactions;
    private final BudgetRepository budgets;
    private final BigDecimal warningThreshold;
    public ReportServiceImpl(ExpenseTransactionRepository t, BudgetRepository b,
            @Value("${expense.budget.warning-threshold:80}") BigDecimal threshold) {
        transactions = t;
        budgets = b;
        warningThreshold = threshold;
    }
    public MonthlySummaryResponse monthly(Integer month, Integer year, ExpenseActor actor) {
        TransactionServiceImpl.requireActor(actor);
        BudgetServiceImpl.validatePeriod(month, year);
        YearMonth period = YearMonth.of(year, month);
        List<ExpenseTransaction> tx =
                transactions.findMonthly(actor.getUserId(), period.atDay(1), period.atEndOfMonth());
        Map<ExpenseCategory, BigDecimal> spent = new EnumMap<>(ExpenseCategory.class);
        BigDecimal income = BigDecimal.ZERO, expense = BigDecimal.ZERO;
        for (ExpenseTransaction t : tx) {
            if (t.getType() == TransactionType.INCOME)
                income = income.add(t.getAmount());
            else {
                expense = expense.add(t.getAmount());
                spent.put(t.getCategory(), spent.getOrDefault(t.getCategory(), BigDecimal.ZERO).add(t.getAmount()));
            }
        }
        List<CategorySummary> summaries = new ArrayList<>();
        for (Budget b : budgets.findByUserIdAndMonthAndYear(actor.getUserId(), month, year)) {
            BigDecimal actual = spent.getOrDefault(b.getCategory(), BigDecimal.ZERO);
            BigDecimal pct =
                    actual.multiply(new BigDecimal("100")).divide(b.getMonthlyLimit(), 2, RoundingMode.HALF_UP);
            CategorySummary s = new CategorySummary();
            s.setCategory(b.getCategory());
            s.setConfiguredBudget(b.getMonthlyLimit());
            s.setActualSpending(actual);
            s.setRemainingBudget(b.getMonthlyLimit().subtract(actual));
            s.setPercentageUsed(pct);
            s.setStatus(pct.compareTo(new BigDecimal("100")) >= 0  ? BudgetStatus.EXCEEDED
                            : pct.compareTo(warningThreshold) >= 0 ? BudgetStatus.WARNING
                                                                   : BudgetStatus.WITHIN_LIMIT);
            summaries.add(s);
        }
        MonthlySummaryResponse r = new MonthlySummaryResponse();
        r.setMonth(month);
        r.setYear(year);
        r.setTotalIncome(income);
        r.setTotalExpenses(expense);
        r.setNetBalance(income.subtract(expense));
        r.setExpensesByCategory(spent);
        r.setCategories(summaries);
        return r;
    }
}
