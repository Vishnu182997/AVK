package com.example.appointment.queue;
import com.example.appointment.appointment.Appointment;
import com.example.appointment.common.BaseEntity;
import java.time.Instant;
import javax.persistence.*;
@Entity
@Table(name = "queue_entry")
public class QueueEntry extends BaseEntity {
  public enum Status { WAITING, CALLED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW }
  @OneToOne(optional = false) private Appointment appointment;
  @Column(nullable = false) private String tokenNumber;
  @Enumerated(EnumType.STRING) @Column(nullable = false) private Status status = Status.WAITING;
  private int queuePosition;
  private int estimatedWaitingTimeMinutes;
  private Instant joinedAt = Instant.now(), calledAt, completedAt;
  protected QueueEntry() {}
  public QueueEntry(Appointment a, String t) {
    appointment = a;
    tokenNumber = t;
  }
  public Appointment getAppointment() {
    return appointment;
  }
  public String getTokenNumber() {
    return tokenNumber;
  }
  public Status getStatus() {
    return status;
  }
  public int getQueuePosition() {
    return queuePosition;
  }
  public int getEstimatedWaitingTimeMinutes() {
    return estimatedWaitingTimeMinutes;
  }
  public Instant getJoinedAt() {
    return joinedAt;
  }
  public void position(int p, int wait) {
    queuePosition = p;
    estimatedWaitingTimeMinutes = wait;
  }
  public void call() {
    status = Status.IN_PROGRESS;
    calledAt = Instant.now();
    appointment.start();
  }
  public void complete() {
    status = Status.COMPLETED;
    completedAt = Instant.now();
  }
  public void noShow() {
    status = Status.NO_SHOW;
    appointment.noShow();
  }
  public void cancel() {
    status = Status.CANCELLED;
  }
}
