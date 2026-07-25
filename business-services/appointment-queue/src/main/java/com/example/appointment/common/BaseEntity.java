package com.example.appointment.common;
import java.time.Instant;
import javax.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt = Instant.now();
  @LastModifiedDate @Column(nullable = false) private Instant updatedAt = Instant.now();
  public Long getId() {
    return id;
  }
  public Instant getCreatedAt() {
    return createdAt;
  }
  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
