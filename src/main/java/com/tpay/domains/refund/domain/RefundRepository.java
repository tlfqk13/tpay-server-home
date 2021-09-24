package com.tpay.domains.refund.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
  RefundEntity findByOrderEntityIdAndRefundStatus(Long orderIndex, RefundStatus status);

  @Query("select refund from RefundEntity refund where refund.orderEntity.franchiseeEntity.id = :franchiseeIndex")
  List<RefundEntity> findAllByFranchiseeIndex(Long franchiseeIndex);
}
