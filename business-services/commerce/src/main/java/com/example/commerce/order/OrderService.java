package com.example.commerce.order;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.example.commerce.audit.*;
import com.example.commerce.cart.*;
import com.example.commerce.common.CommerceException;
import com.example.commerce.config.SecurityConfig;
import com.example.commerce.coupon.*;
import com.example.commerce.product.*;
import com.example.commerce.user.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class OrderService {
  private final OrderRepository orders;
  private final OrderStatusHistoryRepository history;
  private final CartRepository carts;
  private final InventoryRepository inventories;
  private final AddressRepository addresses;
  private final CouponRepository coupons;
  private final CouponUsageRepository usages;
  private final CouponCalculator calculator;
  private final AuditLogRepository audits;
  private final ApplicationEventPublisher events;
  public OrderService(OrderRepository o, OrderStatusHistoryRepository h, CartRepository c,
      InventoryRepository i, AddressRepository a, CouponRepository cp, CouponUsageRepository u,
      CouponCalculator cc, AuditLogRepository al, ApplicationEventPublisher e) {
    orders = o;
    history = h;
    carts = c;
    inventories = i;
    addresses = a;
    coupons = cp;
    usages = u;
    calculator = cc;
    audits = al;
    events = e;
  }
  public OrderDtos.Response checkout(OrderDtos.CheckoutRequest request) {
    User user = SecurityConfig.currentUser();
    Cart cart =
        carts.findDetailedByUserId(user.getId())
            .orElseThrow(() -> new CommerceException(HttpStatus.BAD_REQUEST, "Cart is empty"));
    if (cart.getItems().isEmpty())
      throw new CommerceException(HttpStatus.BAD_REQUEST, "Cart is empty");
    Address address =
        addresses.findByIdAndUserId(request.getAddressId(), user.getId())
            .orElseThrow(
                () -> new CommerceException(HttpStatus.BAD_REQUEST, "Delivery address not found"));
    List<Long> ids = cart.getItems()
                         .stream()
                         .map(i -> i.getProduct().getId())
                         .sorted()
                         .collect(Collectors.toList());
    Map<Long, Inventory> stock = inventories.lockAllByProductIds(ids).stream().collect(
        Collectors.toMap(i -> i.getProduct().getId(), Function.identity()));
    BigDecimal subtotal = BigDecimal.ZERO;
    CustomerOrder order = new CustomerOrder();
    order.setUser(user);
    order.setStatus(OrderStatus.PENDING_PAYMENT);
    order.setDeliveryAddress(snapshot(address));
    for (CartItem ci : cart.getItems()) {
      Product p = ci.getProduct();
      if (!p.isActive())
        throw new CommerceException(
            HttpStatus.CONFLICT, "Product is no longer available: " + p.getId());
      Inventory inv = stock.get(p.getId());
      if (inv == null || inv.getAvailableQuantity() < ci.getQuantity())
        throw new CommerceException(
            HttpStatus.CONFLICT, "Insufficient stock for product: " + p.getId());
      BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())).setScale(2);
      subtotal = subtotal.add(line);
      OrderItem oi = new OrderItem();
      oi.setOrder(order);
      oi.setProductId(p.getId());
      oi.setProductName(p.getName());
      oi.setUnitPrice(p.getPrice());
      oi.setQuantity(ci.getQuantity());
      oi.setLineTotal(line);
      order.getItems().add(oi);
    }
    Coupon coupon = resolveCoupon(request.getCouponCode(), user, subtotal);
    BigDecimal discount = calculator.discount(coupon, subtotal, Instant.now());
    order.setSubtotal(subtotal);
    order.setDiscount(discount);
    order.setTotal(subtotal.subtract(discount).max(BigDecimal.ZERO));
    order.setCouponCode(coupon == null ? null : coupon.getCode());
    order = orders.save(order);
    for (CartItem ci : cart.getItems()) stock.get(ci.getProduct().getId()).deduct(ci.getQuantity());
    if (coupon != null) {
      CouponUsage usage = new CouponUsage();
      usage.setCoupon(coupon);
      usage.setUser(user);
      usage.setOrderId(order.getId());
      usages.save(usage);
    }
    cart.getItems().clear();
    carts.save(cart);
    events.publishEvent(new OrderCreatedEvent(order.getId(), user.getId()));
    return map(order);
  }
  @Transactional(readOnly = true)
  public Page<OrderDtos.Response> mine(Pageable p) {
    return orders.findByUserId(SecurityConfig.currentUser().getId(), p).map(this::map);
  }
  @Transactional(readOnly = true)
  public OrderDtos.Response one(Long id) {
    User u = SecurityConfig.currentUser();
    CustomerOrder o = u.getRole() == User.Role.ADMIN
        ? orders.findById(id).orElseThrow(() -> notFound())
        : orders.findByIdAndUserId(id, u.getId()).orElseThrow(() -> notFound());
    return map(o);
  }
  public OrderDtos.Response transition(Long id, OrderStatus next) {
    User actor = SecurityConfig.currentUser();
    CustomerOrder o = orders.findById(id).orElseThrow(() -> notFound());
    OrderStatus before = o.getStatus();
    if (!before.canTransitionTo(next))
      throw new CommerceException(
          HttpStatus.CONFLICT, "Invalid order transition from " + before + " to " + next);
    o.setStatus(next);
    OrderStatusHistory h = new OrderStatusHistory();
    h.setOrder(o);
    h.setFromStatus(before);
    h.setToStatus(next);
    h.setChangedBy(actor.getEmail());
    history.save(h);
    audit(o, before, next, actor);
    events.publishEvent(new OrderStatusChangedEvent(o.getId(), o.getUser().getId(), next));
    return map(o);
  }
  public OrderDtos.Response cancel(Long id) {
    CustomerOrder o = orders.findByIdAndUserId(id, SecurityConfig.currentUser().getId())
                          .orElseThrow(() -> notFound());
    return transition(o.getId(), OrderStatus.CANCELLED);
  }
  private Coupon resolveCoupon(String code, User u, BigDecimal subtotal) {
    if (code == null || code.trim().isEmpty())
      return null;
    Coupon c =
        coupons.findByCodeIgnoreCase(code.trim())
            .orElseThrow(() -> new CommerceException(HttpStatus.BAD_REQUEST, "Coupon not found"));
    if (usages.countByCouponId(c.getId()) >= c.getUsageLimit()
        || usages.countByCouponIdAndUserId(c.getId(), u.getId()) >= c.getPerUserLimit())
      throw new CommerceException(HttpStatus.BAD_REQUEST, "Coupon usage limit exceeded");
    return c;
  }
  private String snapshot(Address a) {
    return a.getRecipient() + " | " + a.getLine1()
        + (a.getLine2() == null ? "" : " | " + a.getLine2()) + " | " + a.getCity() + " "
        + a.getPostalCode() + " | " + a.getCountryCode();
  }
  private CommerceException notFound() {
    return new CommerceException(HttpStatus.NOT_FOUND, "Order not found");
  }
  private OrderDtos.Response map(CustomerOrder o) {
    List<OrderDtos.Item> items = o.getItems()
                                     .stream()
                                     .map(i
                                         -> new OrderDtos.Item(i.getProductId(), i.getProductName(),
                                             i.getUnitPrice(), i.getQuantity(), i.getLineTotal()))
                                     .collect(Collectors.toList());
    return new OrderDtos.Response(o.getId(), o.getStatus(), o.getSubtotal(), o.getDiscount(),
        o.getTotal(), o.getCouponCode(), o.getDeliveryAddress(), o.getCreatedAt(), items);
  }
  private void audit(CustomerOrder o, OrderStatus b, OrderStatus n, User u) {
    AuditLog a = new AuditLog();
    a.setEntityType("Order");
    a.setEntityId(o.getId().toString());
    a.setAction("STATUS_CHANGE");
    a.setPreviousValue(b.name());
    a.setNewValue(n.name());
    a.setActor(u.getEmail());
    audits.save(a);
  }
}
