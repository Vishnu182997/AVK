package com.example.appointment.queue;
import java.time.Instant;
import java.util.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
@Component
public class QueuePublisher {
  private final SimpMessagingTemplate messages;
  public QueuePublisher(SimpMessagingTemplate m) {
    messages = m;
  }
  public void update(QueueEntry q) {
    Map<String, Object> e = new LinkedHashMap<>();
    e.put("event", "QUEUE_UPDATED");
    e.put("staffId", q.getAppointment().getStaff().getId());
    e.put("serviceId", q.getAppointment().getService().getId());
    e.put("currentToken", q.getTokenNumber());
    e.put("updatedAt", Instant.now());
    messages.convertAndSend("/topic/queue/" + q.getAppointment().getStaff().getId(), e);
    messages.convertAndSend("/topic/queue/service/" + q.getAppointment().getService().getId(), e);
  }
}
