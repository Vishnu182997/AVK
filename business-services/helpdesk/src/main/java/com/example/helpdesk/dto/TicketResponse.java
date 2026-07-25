package com.example.helpdesk.dto;

import java.time.LocalDateTime;

import com.example.helpdesk.model.TicketCategory;
import com.example.helpdesk.model.TicketPriority;
import com.example.helpdesk.model.TicketStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private TicketCategory category;
    private TicketPriority priority;
    private TicketStatus status;
    private String createdBy;
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
}
