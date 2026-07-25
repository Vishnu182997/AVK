package com.example.commerce.product;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import lombok.*;
public final class ProductDtos {
  private ProductDtos() {}
  @Getter
  @Setter
  public static class ProductRequest {
    @NotBlank @Size(max = 160) String name;
    @NotBlank @Size(max = 4000) String description;
    @NotNull @DecimalMin("0.01") @Digits(integer = 17, fraction = 2) BigDecimal price;
    @NotNull Long categoryId;
    @Min(0) int initialQuantity;
    @Size(max = 1000) String imageUrl;
  }
  @Getter
  @Setter
  public static class CategoryRequest {
    @NotBlank @Size(max = 100) String name;
    @Size(max = 500) String description;
  }
  @Getter
  @Setter
  public static class StatusRequest {
    @NotNull Boolean active;
  }
  @Getter
  @AllArgsConstructor
  public static class ProductResponse {
    long id;
    String name;
    String description;
    BigDecimal price;
    String imageUrl;
    long categoryId;
    String category;
    boolean active;
    int availableQuantity;
  }
  @Getter
  @AllArgsConstructor
  public static class CategoryResponse {
    long id;
    String name;
    String description;
  }
}
