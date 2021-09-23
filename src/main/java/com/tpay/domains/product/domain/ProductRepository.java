package com.tpay.domains.product.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
  Optional<ProductEntity> findByNameAndPrice(String name, String price);
}
