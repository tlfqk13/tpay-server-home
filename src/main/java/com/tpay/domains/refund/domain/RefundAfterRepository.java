package com.tpay.domains.refund.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefundAfterRepository extends JpaRepository<RefundAfterEntity, Long> {

}