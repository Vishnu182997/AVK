package com.example.commerce.order;
import com.example.commerce.common.BaseEntity;
import javax.persistence.*;
import lombok.*;
@Entity
@Table(name = "order_status_history",
    indexes = @Index(name = "ix_status_history_order", columnList = "order_id"))
@Getter
@Setter
public class OrderStatusHistory extends BaseEntity {
  @ManyToOne(optional = false, fetch = FetchType.LAZY) private CustomerOrder order;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private OrderStatus fromStatus;
  @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private OrderStatus toStatus;
  @Column(nullable = false, length = 254) private String changedBy;
}
