package com.example.expense.exception;
public class ExpenseException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private final int statusCode;
  public ExpenseException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }
  public int getStatusCode() {
    return statusCode;
  }
}
