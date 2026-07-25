package com.example.commerce.product;
import java.math.BigDecimal;
import javax.persistence.*;
import com.example.commerce.common.BaseEntity;
import com.example.commerce.user.User;
import lombok.*;
@Entity
@Table(name = "product",
    indexes =
    {
      @Index(name = "ix_product_active", columnList = "active")
      , @Index(name = "ix_product_seller", columnList = "seller_id"),
          @Index(name = "ix_product_category", columnList = "category_id")
    })
@Getter
@Setter
public class Product extends BaseEntity {
  @Column(nullable = false, length = 160) private String name;
  @Column(nullable = false, length = 4000) private String description;
  @Column(nullable = false, precision = 19, scale = 2) private BigDecimal price;
  @Column(length = 1000) private String imageUrl;
  @Column(nullable = false) private boolean active = true;
  @ManyToOne(optional = false, fetch = FetchType.LAZY) private Category category;
  @ManyToOne(optional = false, fetch = FetchType.LAZY) private User seller;
}
