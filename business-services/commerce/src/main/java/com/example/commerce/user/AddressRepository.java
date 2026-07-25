package com.example.commerce.user;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AddressRepository extends JpaRepository<Address, Long> {
  Optional<Address> findByIdAndUserId(Long id, Long userId);
}
