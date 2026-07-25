package com.example.helpdesk.dto;

public class TicketActor {
  private final String userId;
  private final boolean ticketManager;

  public TicketActor(String userId, boolean ticketManager) {
    this.userId = userId;
    this.ticketManager = ticketManager;
  }

  public String getUserId() {
    return userId;
  }

  public boolean isTicketManager() {
    return ticketManager;
  }
}
