package com.example.commerce.product;
import java.util.*;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select i from Inventory i where i.product.id in :ids order by i.product.id")
  List<Inventory> lockAllByProductIds(@Param("ids") Collection<Long> ids);
  Optional<Inventory> findByProductId(Long id);
}
