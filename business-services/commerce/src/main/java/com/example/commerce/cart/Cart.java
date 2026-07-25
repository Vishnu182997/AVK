package com.example.commerce.cart;
import java.util.*;
import javax.persistence.*;
import com.example.commerce.common.BaseEntity;
import com.example.commerce.user.User;
import lombok.*;
@Entity
@Table(name = "cart",
    uniqueConstraints = @UniqueConstraint(name = "uk_cart_user", columnNames = "user_id"))
@Getter
@Setter
public class Cart extends BaseEntity {
  @OneToOne(optional = false, fetch = FetchType.LAZY) private User user;
  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CartItem> items = new ArrayList<>();
}
