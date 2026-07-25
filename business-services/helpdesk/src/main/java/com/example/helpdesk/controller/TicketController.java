package com.example.helpdesk.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.helpdesk.dto.CreateTicketRequest;
import com.example.helpdesk.dto.TicketActor;
import com.example.helpdesk.dto.TicketFilterRequest;
import com.example.helpdesk.dto.TicketResponse;
import com.example.helpdesk.dto.UpdateTicketRequest;
import com.example.helpdesk.dto.UpdateTicketStatusRequest;
import com.example.helpdesk.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    public static final String AUTHENTICATED_ACTOR_ATTRIBUTE = "ticketActor";
    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody CreateTicketRequest request,
            @RequestAttribute(AUTHENTICATED_ACTOR_ATTRIBUTE) TicketActor actor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request, actor));
    }

    @GetMapping
    public Page<TicketResponse> findAll(@ModelAttribute TicketFilterRequest filter, Pageable pageable,
            @RequestAttribute(AUTHENTICATED_ACTOR_ATTRIBUTE) TicketActor actor) {
        return service.findAll(filter, pageable, actor);
    }

    @GetMapping("/{id}")
    public TicketResponse findById(@PathVariable Long id,
            @RequestAttribute(AUTHENTICATED_ACTOR_ATTRIBUTE) TicketActor actor) {
        return service.findById(id, actor);
    }

    @PutMapping("/{id}")
    public TicketResponse update(@PathVariable Long id, @Valid @RequestBody UpdateTicketRequest request,
            @RequestAttribute(AUTHENTICATED_ACTOR_ATTRIBUTE) TicketActor actor) {
        return service.update(id, request, actor);
    }

    @PatchMapping("/{id}/status")
    public TicketResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateTicketStatusRequest request,
            @RequestAttribute(AUTHENTICATED_ACTOR_ATTRIBUTE) TicketActor actor) {
        return service.updateStatus(id, request.getStatus(), actor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
            @RequestAttribute(AUTHENTICATED_ACTOR_ATTRIBUTE) TicketActor actor) {
        service.delete(id, actor);
        return ResponseEntity.noContent().build();
    }
}
