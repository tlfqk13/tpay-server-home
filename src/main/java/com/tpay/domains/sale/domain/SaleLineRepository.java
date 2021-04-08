package com.tpay.domains.sale.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleLineRepository extends JpaRepository<SaleLineEntity, Long> {

  List<SaleLineEntity> findAllById(Long saleLineIndex);
}
