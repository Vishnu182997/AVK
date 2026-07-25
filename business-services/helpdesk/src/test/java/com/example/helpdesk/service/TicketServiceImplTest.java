package com.example.helpdesk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.helpdesk.dto.CreateTicketRequest;
import com.example.helpdesk.dto.TicketActor;
import com.example.helpdesk.dto.TicketResponse;
import com.example.helpdesk.dto.UpdateTicketRequest;
import com.example.helpdesk.entity.Ticket;
import com.example.helpdesk.exception.TicketException;
import com.example.helpdesk.model.TicketCategory;
import com.example.helpdesk.model.TicketPriority;
import com.example.helpdesk.model.TicketStatus;
import com.example.helpdesk.repository.TicketRepository;
import com.example.helpdesk.service.impl.TicketServiceImpl;

class TicketServiceImplTest {
    @Mock
    private TicketRepository repository;
    private TicketService service;
    private TicketActor owner;
    private TicketActor manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new TicketServiceImpl(repository);
        owner = new TicketActor("101", false);
        manager = new TicketActor("900", true);
    }

    @Test
    void createsOpenTicket() {
        when(repository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket value = (Ticket) invocation.getArguments()[0];
            value.setId(1L);
            return value;
        });
        TicketResponse response = service.create(createRequest(), owner);
        assertEquals(TicketStatus.OPEN, response.getStatus());
        assertEquals("101", response.getCreatedBy());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    void rejectsInvalidInput() {
        CreateTicketRequest request = createRequest();
        request.setTitle("x");
        assertEquals(400, assertThrows(TicketException.class, () -> service.create(request, owner)).getStatusCode());
    }

    @Test
    void retrievesOwnedTicketAndRejectsAnotherUser() {
        Ticket ticket = ticket(TicketStatus.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        assertEquals(Long.valueOf(1), service.findById(1L, owner).getId());
        assertEquals(403, assertThrows(TicketException.class,
                () -> service.findById(1L, new TicketActor("202", false))).getStatusCode());
    }

    @Test
    void returnsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertEquals(404, assertThrows(TicketException.class, () -> service.findById(99L, owner)).getStatusCode());
    }

    @Test
    void updatesOpenTicketWithoutChangingProtectedFields() {
        Ticket ticket = ticket(TicketStatus.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        when(repository.save(ticket)).thenReturn(ticket);
        UpdateTicketRequest request = updateRequest();
        TicketResponse response = service.update(1L, request, owner);
        assertEquals("Updated ticket", response.getTitle());
        assertEquals(TicketStatus.OPEN, response.getStatus());
        assertEquals("101", response.getCreatedBy());
    }

    @Test
    void enforcesWorkflowAndStatusPermission() {
        Ticket ticket = ticket(TicketStatus.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        assertEquals(403, assertThrows(TicketException.class,
                () -> service.updateStatus(1L, TicketStatus.IN_PROGRESS, owner)).getStatusCode());
        assertEquals(400, assertThrows(TicketException.class,
                () -> service.updateStatus(1L, TicketStatus.RESOLVED, manager)).getStatusCode());
    }

    @Test
    void progressesAndResolvesTicket() {
        Ticket ticket = ticket(TicketStatus.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        when(repository.save(ticket)).thenReturn(ticket);
        assertEquals(TicketStatus.IN_PROGRESS,
                service.updateStatus(1L, TicketStatus.IN_PROGRESS, manager).getStatus());
        TicketResponse resolved = service.updateStatus(1L, TicketStatus.RESOLVED, manager);
        assertEquals(TicketStatus.RESOLVED, resolved.getStatus());
        assertNotNull(resolved.getResolvedAt());
    }

    @Test
    void deletesPermittedOpenTicket() {
        Ticket ticket = ticket(TicketStatus.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        service.delete(1L, owner);
        verify(repository).delete(ticket);
    }

    private CreateTicketRequest createRequest() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("Cannot access account");
        request.setDescription("Access is denied after login");
        request.setCategory(TicketCategory.ACCESS);
        request.setPriority(TicketPriority.HIGH);
        return request;
    }

    private UpdateTicketRequest updateRequest() {
        UpdateTicketRequest request = new UpdateTicketRequest();
        request.setTitle("Updated ticket");
        request.setDescription("Updated ticket description");
        request.setCategory(TicketCategory.TECHNICAL);
        request.setPriority(TicketPriority.CRITICAL);
        return request;
    }

    private Ticket ticket(TicketStatus status) {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Cannot access account");
        ticket.setDescription("Access is denied after login");
        ticket.setCategory(TicketCategory.ACCESS);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setStatus(status);
        ticket.setCreatedBy("101");
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticket;
    }
}
