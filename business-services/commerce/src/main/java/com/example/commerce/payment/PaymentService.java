package com.example.commerce.payment;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.UUID;
import com.example.commerce.common.CommerceException;
import com.example.commerce.config.SecurityConfig;
import com.example.commerce.order.*;
import com.example.commerce.user.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class PaymentService {
  private final PaymentRepository payments;
  private final OrderRepository orders;
  private final OrderService orderService;
  private final ApplicationEventPublisher events;
  public PaymentService(
      PaymentRepository p, OrderRepository o, OrderService s, ApplicationEventPublisher e) {
    payments = p;
    orders = o;
    orderService = s;
    events = e;
  }
  public PaymentDtos.Response create(PaymentDtos.Request r) {
    User u = SecurityConfig.currentUser();
    CustomerOrder o =
        orders.findByIdAndUserId(r.getOrderId(), u.getId())
            .orElseThrow(() -> new CommerceException(HttpStatus.NOT_FOUND, "Order not found"));
    String fingerprint = sha(o.getId() + ":" + o.getTotal().toPlainString());
    Payment existing = payments.findByIdempotencyKey(r.getIdempotencyKey()).orElse(null);
    if (existing != null) {
      if (!existing.getRequestFingerprint().equals(fingerprint))
        throw new CommerceException(
            HttpStatus.CONFLICT, "Idempotency key was used for a different request");
      return map(existing);
    }
    if (o.getStatus() != OrderStatus.PENDING_PAYMENT && o.getStatus() != OrderStatus.PAYMENT_FAILED)
      throw new CommerceException(
          HttpStatus.CONFLICT, "Order cannot be paid in its current status");
    Payment p = new Payment();
    p.setOrder(o);
    p.setAmount(o.getTotal());
    p.setIdempotencyKey(r.getIdempotencyKey());
    p.setRequestFingerprint(fingerprint);
    p.setStatus(Payment.Status.PENDING);
    p.setProviderReference("mock_" + UUID.randomUUID());
    p = payments.save(p);
    orderService.transition(o.getId(), OrderStatus.PAYMENT_PROCESSING);
    return map(p);
  }
  public PaymentDtos.Response webhook(PaymentDtos.Webhook r) {
    Payment p =
        payments.findAll()
            .stream()
            .filter(x -> x.getProviderReference().equals(r.getProviderReference()))
            .findFirst()
            .orElseThrow(() -> new CommerceException(HttpStatus.NOT_FOUND, "Payment not found"));
    if (p.getStatus() != Payment.Status.PENDING)
      return map(p);
    p.setStatus(r.getSuccessful() ? Payment.Status.SUCCEEDED : Payment.Status.FAILED);
    orderService.transition(
        p.getOrder().getId(), r.getSuccessful() ? OrderStatus.PAID : OrderStatus.PAYMENT_FAILED);
    events.publishEvent(
        new PaymentEvent(p.getOrder().getId(), p.getOrder().getUser().getId(), r.getSuccessful()));
    return map(p);
  }
  @Transactional(readOnly = true)
  public PaymentDtos.Response one(Long id) {
    User u = SecurityConfig.currentUser();
    Payment p = u.getRole() == User.Role.ADMIN
        ? payments.findById(id).orElseThrow(() -> notFound())
        : payments.findByIdAndOrderUserId(id, u.getId()).orElseThrow(() -> notFound());
    return map(p);
  }
  private PaymentDtos.Response map(Payment p) {
    return new PaymentDtos.Response(
        p.getId(), p.getOrder().getId(), p.getAmount(), p.getStatus(), p.getProviderReference());
  }
  private CommerceException notFound() {
    return new CommerceException(HttpStatus.NOT_FOUND, "Payment not found");
  }
  private String sha(String value) {
    try {
      byte[] d =
          MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
      StringBuilder s = new StringBuilder();
      for (byte b : d) s.append(String.format("%02x", b));
      return s.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }
}
