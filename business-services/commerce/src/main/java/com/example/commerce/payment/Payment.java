package com.example.commerce.payment;
import java.math.BigDecimal;
import javax.persistence.*;
import com.example.commerce.common.BaseEntity;
import com.example.commerce.order.CustomerOrder;
import lombok.*;
@Entity
@Table(name = "payment",
    uniqueConstraints =
        @UniqueConstraint(name = "uk_payment_idempotency", columnNames = "idempotencyKey"),
    indexes = @Index(name = "ix_payment_order", columnList = "order_id"))
@Getter
@Setter
public class Payment extends BaseEntity {
  public enum Status { PENDING, SUCCEEDED, FAILED }
  @ManyToOne(optional = false, fetch = FetchType.LAZY) private CustomerOrder order;
  @Column(nullable = false, length = 100) private String idempotencyKey;
  @Column(nullable = false, length = 64) private String requestFingerprint;
  @Column(nullable = false, precision = 19, scale = 2) private BigDecimal amount;
  @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Status status;
  @Column(nullable = false, length = 100) private String providerReference;
}
