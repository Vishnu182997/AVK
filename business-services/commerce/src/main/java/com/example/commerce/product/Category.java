package com.example.commerce.product;
import com.example.commerce.common.BaseEntity;
import javax.persistence.*;
import lombok.*;
@Entity
@Table(name = "category",
    uniqueConstraints = @UniqueConstraint(name = "uk_category_name", columnNames = "name"))
@Getter
@Setter
public class Category extends BaseEntity {
  @Column(nullable = false, length = 100) private String name;
  @Column(length = 500) private String description;
  @Column(nullable = false) private boolean active = true;
}
