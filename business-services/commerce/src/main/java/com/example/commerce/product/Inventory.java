package com.example.commerce.product;
import javax.persistence.*;
import com.example.commerce.common.BaseEntity;
import lombok.*;
@Entity
@Table(name = "inventory",
    uniqueConstraints =
        @UniqueConstraint(name = "uk_inventory_product", columnNames = "product_id"))
@Getter
@Setter
public class Inventory extends BaseEntity {
  @OneToOne(optional = false, fetch = FetchType.LAZY) private Product product;
  @Column(nullable = false) private int availableQuantity;
  @Column(nullable = false) private int reservedQuantity;
  @Version private long version;
  public void deduct(int quantity) {
    if (quantity < 1 || availableQuantity < quantity)
      throw new IllegalStateException("Insufficient stock");
    availableQuantity -= quantity;
  }
  public void restore(int quantity) {
    availableQuantity = Math.addExact(availableQuantity, quantity);
  }
}
