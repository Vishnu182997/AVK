package com.example.commerce.wishlist;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
  List<WishlistItem> findByUserId(Long userId);
  Optional<WishlistItem> findByUserIdAndProductId(Long userId, Long productId);
}
