package com.example.commerce.order;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.*;
public final class OrderDtos {
  private OrderDtos() {}
  @Getter
  @Setter
  public static class CheckoutRequest {
    @NotNull Long addressId;
    String couponCode;
  }
  @Getter
  @Setter
  public static class StatusRequest {
    @NotNull OrderStatus status;
  }
  @Getter
  @AllArgsConstructor
  public static class Item {
    long productId;
    String productName;
    BigDecimal unitPrice;
    int quantity;
    BigDecimal lineTotal;
  }
  @Getter
  @AllArgsConstructor
  public static class Response {
    long id;
    OrderStatus status;
    BigDecimal subtotal;
    BigDecimal discount;
    BigDecimal total;
    String couponCode;
    String deliveryAddress;
    Instant createdAt;
    List<Item> items;
  }
}
