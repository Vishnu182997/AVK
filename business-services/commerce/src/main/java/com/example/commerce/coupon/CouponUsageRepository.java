package com.example.commerce.coupon;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
  long countByCouponId(Long id);
  long countByCouponIdAndUserId(Long couponId, Long userId);
}
