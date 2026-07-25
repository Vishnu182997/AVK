package com.example.commerce.review;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findByProductId(Long productId, Pageable p);
  Optional<Review> findByUserIdAndProductId(Long userId, Long productId);
}
