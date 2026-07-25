package com.example.expense.service;

import com.example.expense.dto.*;

public interface ReportService {
    MonthlySummaryResponse monthly(Integer month, Integer year, ExpenseActor actor);
}
