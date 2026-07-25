package com.example.commerce.order;
import lombok.*;
@Getter
@AllArgsConstructor
public class OrderStatusChangedEvent {
  private final Long orderId;
  private final Long userId;
  private final OrderStatus status;
}
