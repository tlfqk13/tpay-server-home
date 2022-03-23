package com.tpay.domains.external.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalRepository extends JpaRepository<ExternalRefundEntity, Long> {
}
