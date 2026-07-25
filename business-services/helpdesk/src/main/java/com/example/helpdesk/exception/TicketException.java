package com.example.helpdesk.exception;

public class TicketException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private final int statusCode;

  public TicketException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
