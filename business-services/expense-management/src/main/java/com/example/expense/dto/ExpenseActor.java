package com.example.expense.dto;
public class ExpenseActor {
    private final String userId;
    public ExpenseActor(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
}
