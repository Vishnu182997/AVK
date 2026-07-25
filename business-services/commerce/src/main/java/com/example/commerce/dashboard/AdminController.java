package com.example.commerce.dashboard;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import com.example.commerce.audit.*;
import com.example.commerce.common.CommerceException;
import com.example.commerce.config.SecurityConfig;
import com.example.commerce.coupon.*;
import com.example.commerce.order.*;
import com.example.commerce.product.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Transactional
public class AdminController {
  private final CouponRepository coupons;
  private final InventoryRepository inventory;
  private final OrderRepository orders;
  private final AuditLogRepository audits;
  public AdminController(
      CouponRepository c, InventoryRepository i, OrderRepository o, AuditLogRepository a) {
    coupons = c;
    inventory = i;
    orders = o;
    audits = a;
  }
  @Getter
  @Setter
  public static class CouponRequest {
    @NotBlank @Size(max = 40) String code;
    @NotNull Coupon.Type type;
    @NotNull @DecimalMin("0.01") BigDecimal value;
    @NotNull @DecimalMin("0.00") BigDecimal minimumOrderValue;
    BigDecimal maximumDiscount;
    @NotNull Instant startsAt;
    @NotNull Instant expiresAt;
    @Min(1) int usageLimit;
    @Min(1) int perUserLimit;
    boolean active = true;
  }
  @PostMapping("/coupons")
  ResponseEntity<Coupon> coupon(@Valid @RequestBody CouponRequest r) {
    if (!r.getStartsAt().isBefore(r.getExpiresAt()))
      throw new CommerceException(HttpStatus.BAD_REQUEST, "Coupon start must precede expiry");
    if (r.getType() == Coupon.Type.PERCENTAGE && r.getValue().compareTo(new BigDecimal("100")) > 0)
      throw new CommerceException(HttpStatus.BAD_REQUEST, "Percentage cannot exceed 100");
    Coupon c = new Coupon();
    c.setCode(r.getCode().trim().toUpperCase());
    c.setType(r.getType());
    c.setValue(r.getValue());
    c.setMinimumOrderValue(r.getMinimumOrderValue());
    c.setMaximumDiscount(r.getMaximumDiscount());
    c.setStartsAt(r.getStartsAt());
    c.setExpiresAt(r.getExpiresAt());
    c.setUsageLimit(r.getUsageLimit());
    c.setPerUserLimit(r.getPerUserLimit());
    c.setActive(r.isActive());
    return ResponseEntity.status(201).body(coupons.save(c));
  }
  @GetMapping("/dashboard/sales")
  Map<String, Object> sales(@RequestParam Instant from, @RequestParam Instant to) {
    Map<String, Object> r = new LinkedHashMap<>();
    r.put("totalOrders", orders.count());
    r.put("revenue",
        orders.revenue(Arrays.asList(OrderStatus.PAID, OrderStatus.CONFIRMED, OrderStatus.PACKED,
                           OrderStatus.SHIPPED, OrderStatus.DELIVERED),
            from, to));
    Map<OrderStatus, Long> status = new EnumMap<>(OrderStatus.class);
    for (OrderStatus s : OrderStatus.values()) status.put(s, orders.countByStatus(s));
    r.put("ordersByStatus", status);
    r.put("recentOrders",
        orders.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")))
            .getContent()
            .size());
    r.put("lowStockProducts",
        inventory.findAll().stream().filter(i -> i.getAvailableQuantity() < 10).count());
    return r;
  }
  @GetMapping("/audit-logs")
  Page<AuditLog> audit(Pageable p) {
    return audits.findAll(p);
  }
}
