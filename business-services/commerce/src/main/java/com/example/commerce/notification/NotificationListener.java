package com.example.commerce.notification;
import com.example.commerce.order.*;
import com.example.commerce.payment.PaymentEvent;
import com.example.commerce.user.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.*;
@Component
public class NotificationListener {
  private final UserRepository users;
  private final NotificationRepository notifications;
  public NotificationListener(UserRepository u, NotificationRepository n) {
    users = u;
    notifications = n;
  }
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void order(OrderCreatedEvent e) {
    save(e.getUserId(), "ORDER_CREATED", "Order " + e.getOrderId() + " was created");
  }
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void status(OrderStatusChangedEvent e) {
    save(e.getUserId(), "ORDER_STATUS", "Order " + e.getOrderId() + " changed to " + e.getStatus());
  }
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void payment(PaymentEvent e) {
    save(e.getUserId(), e.isSuccessful() ? "PAYMENT_SUCCESS" : "PAYMENT_FAILURE",
        "Payment for order " + e.getOrderId() + (e.isSuccessful() ? " succeeded" : " failed"));
  }
  private void save(Long userId, String type, String message) {
    User u = users.findById(userId).orElse(null);
    if (u == null)
      return;
    Notification n = new Notification();
    n.setUser(u);
    n.setType(type);
    n.setMessage(message);
    notifications.save(n);
  }
}
