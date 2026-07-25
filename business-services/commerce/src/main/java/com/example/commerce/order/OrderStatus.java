package com.example.commerce.order;
import java.util.*;
public enum OrderStatus { PENDING_PAYMENT,PAYMENT_PROCESSING,PAID,CONFIRMED,PACKED,SHIPPED,DELIVERED,CANCELLED,PAYMENT_FAILED,REFUNDED;
 private static final Map<OrderStatus,Set<OrderStatus>> TRANSITIONS=new EnumMap<>(OrderStatus.class); static { allow(PENDING_PAYMENT,PAYMENT_PROCESSING,CANCELLED);allow(PAYMENT_PROCESSING,PAID,PAYMENT_FAILED);allow(PAYMENT_FAILED,PAYMENT_PROCESSING,CANCELLED);allow(PAID,CONFIRMED,REFUNDED);allow(CONFIRMED,PACKED,CANCELLED,REFUNDED);allow(PACKED,SHIPPED);allow(SHIPPED,DELIVERED); }
 private static void allow(OrderStatus from,OrderStatus... to){TRANSITIONS.put(from,EnumSet.copyOf(Arrays.asList(to)));} public boolean canTransitionTo(OrderStatus next){return next!=null&&TRANSITIONS.getOrDefault(this,Collections.emptySet()).contains(next);}}
