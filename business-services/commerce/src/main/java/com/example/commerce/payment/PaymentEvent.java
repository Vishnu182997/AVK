package com.example.commerce.payment; import lombok.*; @Getter @AllArgsConstructor public class PaymentEvent{private final Long orderId;private final Long userId;private final boolean successful;}
