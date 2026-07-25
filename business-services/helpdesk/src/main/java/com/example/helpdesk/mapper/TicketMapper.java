package com.example.helpdesk.mapper;

import com.example.helpdesk.dto.TicketResponse;
import com.example.helpdesk.entity.Ticket;

public final class TicketMapper {
  private TicketMapper() {}

  public static TicketResponse toResponse(Ticket ticket) {
    TicketResponse response = new TicketResponse();
    response.setId(ticket.getId());
    response.setTitle(ticket.getTitle());
    response.setDescription(ticket.getDescription());
    response.setCategory(ticket.getCategory());
    response.setPriority(ticket.getPriority());
    response.setStatus(ticket.getStatus());
    response.setCreatedBy(ticket.getCreatedBy());
    response.setAssignedTo(ticket.getAssignedTo());
    response.setCreatedAt(ticket.getCreatedAt());
    response.setUpdatedAt(ticket.getUpdatedAt());
    response.setResolvedAt(ticket.getResolvedAt());
    return response;
  }
}
