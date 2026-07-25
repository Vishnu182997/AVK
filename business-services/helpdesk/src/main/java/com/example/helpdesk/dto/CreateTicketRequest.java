package com.example.helpdesk.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.helpdesk.model.TicketCategory;
import com.example.helpdesk.model.TicketPriority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketRequest {
    @NotBlank
    @Size(min = 3, max = 150)
    private String title;
    @NotBlank
    @Size(min = 5, max = 4000)
    private String description;
    @NotNull
    private TicketCategory category;
    @NotNull
    private TicketPriority priority;
}
