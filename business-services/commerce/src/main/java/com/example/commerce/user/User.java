package com.example.commerce.user;
import java.util.*; import javax.persistence.*; import com.example.commerce.common.BaseEntity; import lombok.*;
@Entity @Table(name="commerce_user",uniqueConstraints=@UniqueConstraint(name="uk_user_email",columnNames="email")) @Getter @Setter
public class User extends BaseEntity { @Column(nullable=false,length=254) private String email; @Column(nullable=false) private String passwordHash; @Column(nullable=false,length=100) private String name; @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private Role role=Role.CUSTOMER; @Column(nullable=false) private boolean active=true; public enum Role{CUSTOMER,SELLER,ADMIN} }
