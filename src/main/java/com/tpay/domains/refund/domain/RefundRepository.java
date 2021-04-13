package com.tpay.domains.refund.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
  RefundEntity findBySaleEntityIdAndRefundStatus(Long saleIndex, RefundStatus status);
}
