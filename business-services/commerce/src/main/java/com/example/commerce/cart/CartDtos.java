package com.example.commerce.cart;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.*;
import lombok.*;
public final class CartDtos {
  private CartDtos() {}
  @Getter
  @Setter
  public static class ItemRequest {
    @NotNull Long productId;
    @Min(1) @Max(999) int quantity;
  }
  @Getter
  @Setter
  public static class QuantityRequest {
    @Min(1) @Max(999) int quantity;
  }
  @Getter
  @AllArgsConstructor
  public static class ItemResponse {
    long id;
    long productId;
    String name;
    BigDecimal unitPrice;
    int quantity;
    BigDecimal lineTotal;
  }
  @Getter
  @AllArgsConstructor
  public static class CartResponse {
    List<ItemResponse> items;
    BigDecimal subtotal;
  }
}
