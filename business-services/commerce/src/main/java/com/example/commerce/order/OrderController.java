package com.example.commerce.order;
import javax.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
public class OrderController {
  private final OrderService service;
  public OrderController(OrderService s) {
    service = s;
  }
  @PostMapping("/api/orders")
  @PreAuthorize("hasRole('CUSTOMER')")
  ResponseEntity<OrderDtos.Response> create(@Valid @RequestBody OrderDtos.CheckoutRequest r) {
    return ResponseEntity.status(201).body(service.checkout(r));
  }
  @GetMapping("/api/orders")
  @PreAuthorize("hasRole('CUSTOMER')")
  Page<OrderDtos.Response> all(Pageable p) {
    return service.mine(p);
  }
  @GetMapping("/api/orders/{id}")
  OrderDtos.Response one(@PathVariable Long id) {
    return service.one(id);
  }
  @PostMapping("/api/orders/{id}/cancel")
  @PreAuthorize("hasRole('CUSTOMER')")
  OrderDtos.Response cancel(@PathVariable Long id) {
    return service.cancel(id);
  }
  @PatchMapping("/api/admin/orders/{id}/status")
  @PreAuthorize("hasRole('ADMIN')")
  OrderDtos.Response status(@PathVariable Long id, @Valid @RequestBody OrderDtos.StatusRequest r) {
    return service.transition(id, r.getStatus());
  }
}
