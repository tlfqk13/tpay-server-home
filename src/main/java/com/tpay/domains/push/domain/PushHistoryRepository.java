package com.tpay.domains.push.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PushHistoryRepository extends JpaRepository<PushHistoryEntity, Long> {
    Optional<List<PushHistoryEntity>> findByUserIdOrderByIdDesc(Long userId);

    long countByUserIdAndIsRead(Long userId, Boolean isRead);
}
