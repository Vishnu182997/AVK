package com.example.commerce.cart;
import javax.persistence.*; import com.example.commerce.common.BaseEntity; import com.example.commerce.product.Product; import lombok.*;
@Entity @Table(name="cart_item",uniqueConstraints=@UniqueConstraint(name="uk_cart_product",columnNames={"cart_id","product_id"})) @Getter @Setter public class CartItem extends BaseEntity { @ManyToOne(optional=false,fetch=FetchType.LAZY) private Cart cart; @ManyToOne(optional=false,fetch=FetchType.LAZY) private Product product; @Column(nullable=false) private int quantity; }
