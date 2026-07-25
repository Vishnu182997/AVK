package com.example.helpdesk.dto;

import com.example.helpdesk.model.TicketCategory;
import com.example.helpdesk.model.TicketPriority;
import com.example.helpdesk.model.TicketStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketFilterRequest {
    private TicketStatus status;
    private TicketPriority priority;
    private TicketCategory category;
    private String createdBy;
}
