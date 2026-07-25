package com.example.commerce.order;
import java.math.BigDecimal;
import java.util.*;
import javax.persistence.*;
import com.example.commerce.common.BaseEntity;
import com.example.commerce.user.User;
import lombok.*;
@Entity
@Table(name = "customer_order",
    indexes =
    {
      @Index(name = "ix_order_user", columnList = "user_id")
      , @Index(name = "ix_order_status", columnList = "status"),
          @Index(name = "ix_order_created", columnList = "createdAt")
    })
@Getter
@Setter
public class CustomerOrder extends BaseEntity {
  @ManyToOne(optional = false, fetch = FetchType.LAZY) private User user;
  @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private OrderStatus status;
  @Column(nullable = false, precision = 19, scale = 2) private BigDecimal subtotal;
  @Column(nullable = false, precision = 19, scale = 2) private BigDecimal discount;
  @Column(nullable = false, precision = 19, scale = 2) private BigDecimal total;
  @Column(length = 40) private String couponCode;
  @Column(nullable = false, length = 500) private String deliveryAddress;
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();
}
