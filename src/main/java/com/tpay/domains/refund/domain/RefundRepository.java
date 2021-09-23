package com.tpay.domains.refund.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
  RefundEntity findByOrderEntityIdAndRefundStatus(Long orderIndex, RefundStatus status);
}
