package com.example.commerce.notification;
import javax.persistence.*; import com.example.commerce.common.BaseEntity; import com.example.commerce.user.User; import lombok.*;
@Entity @Table(name="notification",indexes=@Index(name="ix_notification_user",columnList="user_id")) @Getter @Setter public class Notification extends BaseEntity { @ManyToOne(optional=false,fetch=FetchType.LAZY) private User user; @Column(nullable=false,length=40) private String type; @Column(nullable=false,length=1000) private String message; @Column(nullable=false) private boolean delivered; }
