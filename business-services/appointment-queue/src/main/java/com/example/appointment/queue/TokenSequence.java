package com.example.appointment.queue;
import java.time.LocalDate;
import javax.persistence.*;
@Entity
@Table(name = "queue_token_sequence",
    uniqueConstraints = @UniqueConstraint(columnNames = {"sequence_date", "service_id"}))
public class TokenSequence {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(name = "sequence_date") private LocalDate date;
  private Long serviceId;
  private long nextValue = 1;
  protected TokenSequence() {}
  public TokenSequence(LocalDate d, Long s) {
    date = d;
    serviceId = s;
  }
  public long take() {
    return nextValue++;
  }
}
