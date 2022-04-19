package com.tpay.domains.push.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushHistoryRepository extends JpaRepository<PushHistoryEntity, Long> {
}
