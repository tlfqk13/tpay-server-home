package com.tpay.domains.order.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
  Optional<List<OrderEntity>> findAllByFranchiseeEntityId(Long franchiseeId);
}
