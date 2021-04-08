package com.tpay.domains.sale.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
  List<SaleEntity> findAllByFranchiseeEntityId(Long franchiseeId);

  List<SaleEntity> findAllByCustomerEntityId(Long customerIndex);
}
