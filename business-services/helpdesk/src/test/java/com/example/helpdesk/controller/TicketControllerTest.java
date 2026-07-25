package com.example.helpdesk.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.example.helpdesk.dto.CreateTicketRequest;
import com.example.helpdesk.dto.TicketActor;
import com.example.helpdesk.dto.TicketResponse;
import com.example.helpdesk.dto.UpdateTicketStatusRequest;
import com.example.helpdesk.exception.TicketException;
import com.example.helpdesk.model.TicketStatus;
import com.example.helpdesk.service.TicketService;

class TicketControllerTest {
    private TicketService service;
    private TicketController controller;
    private TicketActor actor;

    @BeforeEach
    void setUp() {
        service = mock(TicketService.class);
        controller = new TicketController(service);
        actor = new TicketActor("101", false);
    }

    @Test
    void createReturnsCreated() {
        CreateTicketRequest request = new CreateTicketRequest();
        TicketResponse ticket = new TicketResponse();
        when(service.create(request, actor)).thenReturn(ticket);
        ResponseEntity<TicketResponse> response = controller.create(request, actor);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(ticket, response.getBody());
    }

    @Test
    void getByIdReturnsTicket() {
        TicketResponse ticket = new TicketResponse();
        when(service.findById(1L, actor)).thenReturn(ticket);
        assertEquals(ticket, controller.findById(1L, actor));
    }

    @Test
    void statusUpdateDelegatesToDedicatedOperation() {
        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest();
        request.setStatus(TicketStatus.IN_PROGRESS);
        TicketResponse ticket = new TicketResponse();
        when(service.updateStatus(1L, TicketStatus.IN_PROGRESS, actor)).thenReturn(ticket);
        assertEquals(ticket, controller.updateStatus(1L, request, actor));
    }

    @Test
    void deleteReturnsNoContent() {
        assertEquals(204, controller.delete(1L, actor).getStatusCodeValue());
        verify(service).delete(1L, actor);
    }

    @Test
    void exceptionHandlerPreservesNotFoundAndForbiddenStatus() {
        TicketExceptionHandler handler = new TicketExceptionHandler();
        assertEquals(404, handler.handleTicketException(new TicketException("missing", 404)).getStatusCodeValue());
        assertEquals(403, handler.handleTicketException(new TicketException("forbidden", 403)).getStatusCodeValue());
    }

    @Test
    void exceptionHandlerPreservesInvalidStatusResponse() {
        TicketExceptionHandler handler = new TicketExceptionHandler();
        assertEquals(400, handler.handleTicketException(new TicketException("invalid transition", 400))
                .getStatusCodeValue());
    }
}
