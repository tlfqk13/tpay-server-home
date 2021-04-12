package com.tpay.domains.refund.domain;

import com.tpay.domains.sale.domain.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
    RefundEntity findBySaleEntityId(Long saleIndex);
}
