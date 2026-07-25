package com.example.helpdesk.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.example.helpdesk.model.TicketCategory;
import com.example.helpdesk.model.TicketPriority;
import com.example.helpdesk.model.TicketStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Helpdesk_Ticket", indexes = {
        @Index(name = "IX_Ticket_Status", columnList = "Status"),
        @Index(name = "IX_Ticket_Priority", columnList = "Priority"),
        @Index(name = "IX_Ticket_Category", columnList = "Category"),
        @Index(name = "IX_Ticket_Created_By", columnList = "Created_By") })
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Ticket_Id")
    private Long id;

    @Column(name = "Title", nullable = false, length = 150)
    private String title;

    @Column(name = "Description", nullable = false, length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "Category", nullable = false, length = 30)
    private TicketCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "Priority", nullable = false, length = 20)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 20)
    private TicketStatus status;

    @Column(name = "Created_By", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "Assigned_To", length = 100)
    private String assignedTo;

    @Column(name = "Created_At", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "Updated_At", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "Resolved_At")
    private LocalDateTime resolvedAt;
}
