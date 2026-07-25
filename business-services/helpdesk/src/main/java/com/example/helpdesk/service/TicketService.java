package com.example.helpdesk.service;

import com.example.helpdesk.dto.CreateTicketRequest;
import com.example.helpdesk.dto.TicketActor;
import com.example.helpdesk.dto.TicketFilterRequest;
import com.example.helpdesk.dto.TicketResponse;
import com.example.helpdesk.dto.UpdateTicketRequest;
import com.example.helpdesk.model.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {
  TicketResponse create(CreateTicketRequest request, TicketActor actor);
  Page<TicketResponse> findAll(TicketFilterRequest filter, Pageable pageable, TicketActor actor);
  TicketResponse findById(Long id, TicketActor actor);
  TicketResponse update(Long id, UpdateTicketRequest request, TicketActor actor);
  TicketResponse updateStatus(Long id, TicketStatus status, TicketActor actor);
  void delete(Long id, TicketActor actor);
}
