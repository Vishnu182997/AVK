package com.example.helpdesk.model;

public enum TicketStatus {
  OPEN,
  IN_PROGRESS,
  RESOLVED,
  CLOSED;

  public boolean canTransitionTo(TicketStatus target) {
    if (target == null) {
      return false;
    }
    switch (this) {
      case OPEN:
        return target == IN_PROGRESS || target == CLOSED;
      case IN_PROGRESS:
        return target == RESOLVED || target == CLOSED;
      case RESOLVED:
        return target == CLOSED;
      default:
        return false;
    }
  }
}
