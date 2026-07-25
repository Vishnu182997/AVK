package com.example.commerce.product;
import java.math.BigDecimal;
import javax.persistence.criteria.Predicate;
import com.example.commerce.audit.*;
import com.example.commerce.common.CommerceException;
import com.example.commerce.config.SecurityConfig;
import com.example.commerce.user.User;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class ProductService {
  private final ProductRepository products;
  private final CategoryRepository categories;
  private final InventoryRepository inventory;
  private final AuditLogRepository audits;
  public ProductService(
      ProductRepository p, CategoryRepository c, InventoryRepository i, AuditLogRepository a) {
    products = p;
    categories = c;
    inventory = i;
    audits = a;
  }
  public ProductDtos.ProductResponse create(ProductDtos.ProductRequest r) {
    User actor = SecurityConfig.currentUser();
    Product p = new Product();
    apply(p, r);
    p.setSeller(actor);
    p = products.save(p);
    Inventory i = new Inventory();
    i.setProduct(p);
    i.setAvailableQuantity(r.getInitialQuantity());
    inventory.save(i);
    audit("Product", p.getId(), "CREATE", null, p.getName(), actor);
    return map(p, i.getAvailableQuantity());
  }
  public ProductDtos.ProductResponse update(Long id, ProductDtos.ProductRequest r) {
    Product p = owned(id);
    String before = p.getName() + ":" + p.getPrice();
    apply(p, r);
    p = products.save(p);
    audit("Product", id, "UPDATE", before, p.getName() + ":" + p.getPrice(),
        SecurityConfig.currentUser());
    return map(p, inventory.findByProductId(id).map(Inventory::getAvailableQuantity).orElse(0));
  }
  public ProductDtos.ProductResponse status(Long id, boolean active) {
    Product p = owned(id);
    p.setActive(active);
    return map(products.save(p),
        inventory.findByProductId(id).map(Inventory::getAvailableQuantity).orElse(0));
  }
  @Transactional(readOnly = true)
  public ProductDtos.ProductResponse one(Long id) {
    Product p =
        products.findById(id)
            .filter(Product::isActive)
            .orElseThrow(() -> new CommerceException(HttpStatus.NOT_FOUND, "Product not found"));
    return map(p, inventory.findByProductId(id).map(Inventory::getAvailableQuantity).orElse(0));
  }
  @Transactional(readOnly = true)
  public Page<ProductDtos.ProductResponse> search(
      String query, Long category, BigDecimal min, BigDecimal max, Pageable pageable) {
    Specification<Product> s = (root, q, b) -> {
      Predicate x = b.isTrue(root.get("active"));
      if (query != null && !query.trim().isEmpty())
        x = b.and(x, b.like(b.lower(root.get("name")), "%" + query.toLowerCase() + "%"));
      if (category != null)
        x = b.and(x, b.equal(root.get("category").get("id"), category));
      if (min != null)
        x = b.and(x, b.greaterThanOrEqualTo(root.get("price"), min));
      if (max != null)
        x = b.and(x, b.lessThanOrEqualTo(root.get("price"), max));
      return x;
    };
    return products.findAll(s, pageable)
        .map(p
            -> map(p,
                inventory.findByProductId(p.getId())
                    .map(Inventory::getAvailableQuantity)
                    .orElse(0)));
  }
  private void apply(Product p, ProductDtos.ProductRequest r) {
    Category c =
        categories.findById(r.getCategoryId())
            .filter(Category::isActive)
            .orElseThrow(
                () -> new CommerceException(HttpStatus.BAD_REQUEST, "Active category not found"));
    p.setName(r.getName().trim());
    p.setDescription(r.getDescription().trim());
    p.setPrice(r.getPrice());
    p.setImageUrl(r.getImageUrl());
    p.setCategory(c);
  }
  private Product owned(Long id) {
    Product p = products.findById(id).orElseThrow(
        () -> new CommerceException(HttpStatus.NOT_FOUND, "Product not found"));
    User u = SecurityConfig.currentUser();
    if (u.getRole() != User.Role.ADMIN && !p.getSeller().getId().equals(u.getId()))
      throw new CommerceException(HttpStatus.FORBIDDEN, "Product belongs to another seller");
    return p;
  }
  private ProductDtos.ProductResponse map(Product p, int qty) {
    return new ProductDtos.ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice(),
        p.getImageUrl(), p.getCategory().getId(), p.getCategory().getName(), p.isActive(), qty);
  }
  private void audit(String type, Long id, String action, String before, String after, User u) {
    AuditLog a = new AuditLog();
    a.setEntityType(type);
    a.setEntityId(id.toString());
    a.setAction(action);
    a.setPreviousValue(before);
    a.setNewValue(after);
    a.setActor(u.getEmail());
    audits.save(a);
  }
}
