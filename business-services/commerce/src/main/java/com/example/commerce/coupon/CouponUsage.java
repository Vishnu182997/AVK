package com.example.commerce.coupon;
import javax.persistence.*;
import com.example.commerce.common.BaseEntity;
import com.example.commerce.user.User;
import lombok.*;
@Entity
@Table(name = "coupon_usage",
    indexes =
    {
      @Index(name = "ix_coupon_usage_coupon", columnList = "coupon_id")
      , @Index(name = "ix_coupon_usage_user", columnList = "user_id")
    })
@Getter
@Setter
public class CouponUsage extends BaseEntity {
  @ManyToOne(optional = false, fetch = FetchType.LAZY) private Coupon coupon;
  @ManyToOne(optional = false, fetch = FetchType.LAZY) private User user;
  @Column(nullable = false) private Long orderId;
}
