package com.example.commerce.user;
import javax.persistence.*; import com.example.commerce.common.BaseEntity; import lombok.*;
@Entity @Table(name="address",indexes=@Index(name="ix_address_user",columnList="user_id")) @Getter @Setter
public class Address extends BaseEntity { @ManyToOne(optional=false,fetch=FetchType.LAZY) private User user; @Column(nullable=false,length=120) private String recipient; @Column(nullable=false,length=200) private String line1; @Column(length=200) private String line2; @Column(nullable=false,length=100) private String city; @Column(nullable=false,length=30) private String postalCode; @Column(nullable=false,length=2) private String countryCode; }
