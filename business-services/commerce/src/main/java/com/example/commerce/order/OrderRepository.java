package com.example.commerce.order;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface OrderRepository
    extends JpaRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder> {
  Page<CustomerOrder> findByUserId(Long userId, Pageable p);
  Optional<CustomerOrder> findByIdAndUserId(Long id, Long userId);
  long countByStatus(OrderStatus s);
  @Query("select coalesce(sum(o.total),0) from CustomerOrder o where o.status in :statuses and "
      + "o.createdAt between :from and :to")
  BigDecimal
  revenue(@Param("statuses") Collection<OrderStatus> statuses, @Param("from") Instant from,
      @Param("to") Instant to);
}
