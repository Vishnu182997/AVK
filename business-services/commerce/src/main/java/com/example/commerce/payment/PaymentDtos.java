package com.example.commerce.payment;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import lombok.*;
public final class PaymentDtos {
  private PaymentDtos() {}
  @Getter
  @Setter
  public static class Request {
    @NotNull Long orderId;
    @NotBlank @Size(min = 8, max = 100) String idempotencyKey;
  }
  @Getter
  @Setter
  public static class Webhook {
    @NotBlank String providerReference;
    @NotNull Boolean successful;
  }
  @Getter
  @AllArgsConstructor
  public static class Response {
    long id;
    long orderId;
    BigDecimal amount;
    Payment.Status status;
    String providerReference;
  }
}
