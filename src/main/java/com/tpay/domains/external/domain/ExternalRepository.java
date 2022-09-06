package com.tpay.domains.external.domain;

import com.tpay.domains.refund.domain.RefundEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExternalRepository extends JpaRepository<ExternalRefundEntity, Long> {

    Optional<ExternalRefundEntity> findByRefundEntity(RefundEntity refundEntity);
}
