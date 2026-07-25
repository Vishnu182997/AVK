package com.example.helpdesk.dto;

import javax.validation.constraints.NotNull;

import com.example.helpdesk.model.TicketStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTicketStatusRequest {
    @NotNull
    private TicketStatus status;
}
