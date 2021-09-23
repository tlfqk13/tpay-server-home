package com.tpay.domains.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleLineRepository extends JpaRepository<OrderLineEntity, Long> {

  List<OrderLineEntity> findAllById(Long saleLineIndex);
}
