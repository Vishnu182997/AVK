package com.example.commerce.cart;
import javax.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {
  private final CartService service;
  public CartController(CartService s) {
    service = s;
  }
  @GetMapping
  CartDtos.CartResponse get() {
    return service.get();
  }
  @PostMapping("/items")
  ResponseEntity<CartDtos.CartResponse> add(@Valid @RequestBody CartDtos.ItemRequest r) {
    return ResponseEntity.status(201).body(service.add(r));
  }
  @PatchMapping("/items/{id}")
  CartDtos.CartResponse update(
      @PathVariable Long id, @Valid @RequestBody CartDtos.QuantityRequest r) {
    return service.update(id, r.getQuantity());
  }
  @DeleteMapping("/items/{id}")
  ResponseEntity<Void> remove(@PathVariable Long id) {
    service.remove(id);
    return ResponseEntity.noContent().build();
  }
  @DeleteMapping
  ResponseEntity<Void> clear() {
    service.clear();
    return ResponseEntity.noContent().build();
  }
}
