package com.example.commerce.wishlist;
import javax.persistence.*;
import com.example.commerce.common.BaseEntity;
import com.example.commerce.product.Product;
import com.example.commerce.user.User;
import lombok.*;
@Entity
@Table(name = "wishlist_item",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_wishlist_user_product", columnNames = {"user_id", "product_id"}))
@Getter
@Setter
public class WishlistItem extends BaseEntity {
  @ManyToOne(optional = false, fetch = FetchType.LAZY) private User user;
  @ManyToOne(optional = false, fetch = FetchType.LAZY) private Product product;
}
