package com.example.helpdesk.service.impl;

import java.time.LocalDateTime;

import javax.persistence.criteria.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.helpdesk.dto.CreateTicketRequest;
import com.example.helpdesk.dto.TicketActor;
import com.example.helpdesk.dto.TicketFilterRequest;
import com.example.helpdesk.dto.TicketResponse;
import com.example.helpdesk.dto.UpdateTicketRequest;
import com.example.helpdesk.entity.Ticket;
import com.example.helpdesk.exception.TicketException;
import com.example.helpdesk.mapper.TicketMapper;
import com.example.helpdesk.model.TicketStatus;
import com.example.helpdesk.repository.TicketRepository;
import com.example.helpdesk.service.TicketService;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    private static final Logger LOGGER = LogManager.getLogger(TicketServiceImpl.class);
    private final TicketRepository repository;

    public TicketServiceImpl(TicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public TicketResponse create(CreateTicketRequest request, TicketActor actor) {
        requireActor(actor);
        validate(request == null ? null : request.getTitle(), request == null ? null : request.getDescription(),
                request == null ? null : request.getCategory(), request == null ? null : request.getPriority());
        LocalDateTime now = LocalDateTime.now();
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle().trim());
        ticket.setDescription(request.getDescription().trim());
        ticket.setCategory(request.getCategory());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedBy(actor.getUserId());
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);
        Ticket saved = repository.save(ticket);
        LOGGER.info("Created help desk ticket {}", saved.getId());
        return TicketMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketResponse> findAll(final TicketFilterRequest filter, Pageable pageable, final TicketActor actor) {
        requireActor(actor);
        Specification<Ticket> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (!actor.isTicketManager()) {
                predicate = builder.and(predicate, builder.equal(root.get("createdBy"), actor.getUserId()));
            } else if (filter != null && filter.getCreatedBy() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("createdBy"), filter.getCreatedBy()));
            }
            if (filter != null && filter.getStatus() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("status"), filter.getStatus()));
            }
            if (filter != null && filter.getPriority() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("priority"), filter.getPriority()));
            }
            if (filter != null && filter.getCategory() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("category"), filter.getCategory()));
            }
            return predicate;
        };
        return repository.findAll(specification, pageable).map(TicketMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse findById(Long id, TicketActor actor) {
        Ticket ticket = getTicket(id);
        requireAccess(ticket, actor);
        return TicketMapper.toResponse(ticket);
    }

    @Override
    public TicketResponse update(Long id, UpdateTicketRequest request, TicketActor actor) {
        validate(request == null ? null : request.getTitle(), request == null ? null : request.getDescription(),
                request == null ? null : request.getCategory(), request == null ? null : request.getPriority());
        Ticket ticket = getTicket(id);
        requireAccess(ticket, actor);
        if (!actor.isTicketManager() && ticket.getStatus() != TicketStatus.OPEN) {
            throw new TicketException("Only open tickets can be updated by their creator", 403);
        }
        ticket.setTitle(request.getTitle().trim());
        ticket.setDescription(request.getDescription().trim());
        ticket.setCategory(request.getCategory());
        ticket.setPriority(request.getPriority());
        ticket.setUpdatedAt(LocalDateTime.now());
        return TicketMapper.toResponse(repository.save(ticket));
    }

    @Override
    public TicketResponse updateStatus(Long id, TicketStatus status, TicketActor actor) {
        requireActor(actor);
        if (!actor.isTicketManager()) {
            LOGGER.warn("User attempted an unauthorized ticket status change");
            throw new TicketException("Ticket status changes require ticket management permission", 403);
        }
        Ticket ticket = getTicket(id);
        if (!ticket.getStatus().canTransitionTo(status)) {
            throw new TicketException("Invalid ticket status transition from " + ticket.getStatus() + " to " + status, 400);
        }
        ticket.setStatus(status);
        ticket.setUpdatedAt(LocalDateTime.now());
        if (status == TicketStatus.RESOLVED) {
            ticket.setResolvedAt(ticket.getUpdatedAt());
        }
        Ticket saved = repository.save(ticket);
        LOGGER.info("Changed help desk ticket {} status to {}", saved.getId(), status);
        return TicketMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id, TicketActor actor) {
        Ticket ticket = getTicket(id);
        requireAccess(ticket, actor);
        if (!actor.isTicketManager() && ticket.getStatus() != TicketStatus.OPEN) {
            throw new TicketException("Only open tickets can be deleted by their creator", 403);
        }
        repository.delete(ticket);
        LOGGER.info("Deleted help desk ticket {}", id);
    }

    private Ticket getTicket(Long id) {
        if (id == null) {
            throw new TicketException("Ticket id is required", 400);
        }
        return repository.findById(id).orElseThrow(() -> new TicketException("Ticket not found: " + id, 404));
    }

    private void requireAccess(Ticket ticket, TicketActor actor) {
        requireActor(actor);
        if (!actor.isTicketManager() && !actor.getUserId().equals(ticket.getCreatedBy())) {
            LOGGER.warn("User attempted unauthorized access to ticket {}", ticket.getId());
            throw new TicketException("Access to this ticket is forbidden", 403);
        }
    }

    private void requireActor(TicketActor actor) {
        if (actor == null || actor.getUserId() == null || actor.getUserId().trim().isEmpty()) {
            throw new TicketException("Authentication is required", 401);
        }
    }

    private void validate(String title, String description, Object category, Object priority) {
        if (title == null || title.trim().length() < 3 || title.trim().length() > 150) {
            throw new TicketException("Title must contain between 3 and 150 characters", 400);
        }
        if (description == null || description.trim().length() < 5 || description.trim().length() > 4000) {
            throw new TicketException("Description must contain between 5 and 4000 characters", 400);
        }
        if (category == null || priority == null) {
            throw new TicketException("Category and priority are required", 400);
        }
    }
}
