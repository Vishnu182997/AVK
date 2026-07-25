package com.example.commerce.order; import lombok.*; @Getter @AllArgsConstructor public class OrderCreatedEvent {private final Long orderId;private final Long userId;}
