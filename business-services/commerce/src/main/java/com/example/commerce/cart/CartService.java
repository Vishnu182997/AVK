package com.example.commerce.cart;
import java.math.BigDecimal;
import java.util.*;
import com.example.commerce.common.CommerceException;
import com.example.commerce.config.SecurityConfig;
import com.example.commerce.product.*;
import com.example.commerce.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class CartService {
  private final CartRepository carts;
  private final ProductRepository products;
  private final InventoryRepository inventory;
  public CartService(CartRepository c, ProductRepository p, InventoryRepository i) {
    carts = c;
    products = p;
    inventory = i;
  }
  public CartDtos.CartResponse get() {
    return map(cart());
  }
  public CartDtos.CartResponse add(CartDtos.ItemRequest r) {
    Cart c = cart();
    Product p =
        products.findById(r.getProductId())
            .filter(Product::isActive)
            .orElseThrow(
                () -> new CommerceException(HttpStatus.BAD_REQUEST, "Product is not purchasable"));
    int available =
        inventory.findByProductId(p.getId()).map(Inventory::getAvailableQuantity).orElse(0);
    CartItem item = c.getItems()
                        .stream()
                        .filter(x -> x.getProduct().getId().equals(p.getId()))
                        .findFirst()
                        .orElse(null);
    int quantity = r.getQuantity() + (item == null ? 0 : item.getQuantity());
    if (quantity > available)
      throw new CommerceException(HttpStatus.CONFLICT, "Insufficient stock");
    if (item == null) {
      item = new CartItem();
      item.setCart(c);
      item.setProduct(p);
      c.getItems().add(item);
    }
    item.setQuantity(quantity);
    return map(carts.save(c));
  }
  public CartDtos.CartResponse update(Long itemId, int quantity) {
    Cart c = cart();
    CartItem i = item(c, itemId);
    int available = inventory.findByProductId(i.getProduct().getId())
                        .map(Inventory::getAvailableQuantity)
                        .orElse(0);
    if (quantity > available)
      throw new CommerceException(HttpStatus.CONFLICT, "Insufficient stock");
    i.setQuantity(quantity);
    return map(carts.save(c));
  }
  public void remove(Long id) {
    Cart c = cart();
    c.getItems().remove(item(c, id));
    carts.save(c);
  }
  public void clear() {
    Cart c = cart();
    c.getItems().clear();
    carts.save(c);
  }
  private Cart cart() {
    User u = SecurityConfig.currentUser();
    return carts.findDetailedByUserId(u.getId()).orElseGet(() -> {
      Cart c = new Cart();
      c.setUser(u);
      return carts.save(c);
    });
  }
  private CartItem item(Cart c, Long id) {
    return c.getItems()
        .stream()
        .filter(x -> x.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new CommerceException(HttpStatus.NOT_FOUND, "Cart item not found"));
  }
  private CartDtos.CartResponse map(Cart c) {
    List<CartDtos.ItemResponse> result = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;
    for (CartItem i : c.getItems()) {
      BigDecimal line = i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity()));
      total = total.add(line);
      result.add(new CartDtos.ItemResponse(i.getId(), i.getProduct().getId(),
          i.getProduct().getName(), i.getProduct().getPrice(), i.getQuantity(), line));
    }
    return new CartDtos.CartResponse(result, total);
  }
}
