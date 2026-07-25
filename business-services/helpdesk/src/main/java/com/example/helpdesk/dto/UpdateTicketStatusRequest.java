package com.example.helpdesk.dto;

import com.example.helpdesk.model.TicketStatus;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTicketStatusRequest {
  @NotNull private TicketStatus status;
}
