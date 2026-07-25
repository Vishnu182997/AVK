package com.example.commerce.coupon;
import java.math.*;
import java.time.Instant;
import com.example.commerce.common.CommerceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
@Component
public class CouponCalculator {
  public BigDecimal discount(Coupon c, BigDecimal subtotal, Instant now) {
    if (c == null)
      return BigDecimal.ZERO;
    if (!c.isActive() || now.isBefore(c.getStartsAt()) || !now.isBefore(c.getExpiresAt()))
      throw new CommerceException(HttpStatus.BAD_REQUEST, "Coupon is inactive or expired");
    if (subtotal.compareTo(c.getMinimumOrderValue()) < 0)
      throw new CommerceException(HttpStatus.BAD_REQUEST, "Order does not meet coupon minimum");
    BigDecimal d = c.getType() == Coupon.Type.FIXED
        ? c.getValue()
        : subtotal.multiply(c.getValue()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    if (c.getMaximumDiscount() != null && d.compareTo(c.getMaximumDiscount()) > 0)
      d = c.getMaximumDiscount();
    return d.min(subtotal).setScale(2, RoundingMode.HALF_UP);
  }
}
