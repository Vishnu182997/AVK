package com.example.commerce.coupon;
import com.example.commerce.common.BaseEntity;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import lombok.*;
@Entity
@Table(name = "coupon",
    uniqueConstraints = @UniqueConstraint(name = "uk_coupon_code", columnNames = "code"))
@Getter
@Setter
public class Coupon extends BaseEntity {
  public enum Type { FIXED, PERCENTAGE }
  @Column(nullable = false, length = 40) private String code;
  @Enumerated(EnumType.STRING) @Column(nullable = false, length = 15) private Type type;
  @Column(nullable = false, precision = 19, scale = 2) private BigDecimal value;
  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal minimumOrderValue = BigDecimal.ZERO;
  @Column(precision = 19, scale = 2) private BigDecimal maximumDiscount;
  @Column(nullable = false) private Instant startsAt;
  @Column(nullable = false) private Instant expiresAt;
  @Column(nullable = false) private int usageLimit;
  @Column(nullable = false) private int perUserLimit = 1;
  @Column(nullable = false) private boolean active = true;
}
