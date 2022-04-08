package com.tpay.domains.push.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PushHistoryRepository extends JpaRepository<PushHistoryEntity, Long> {

    @Query(value = "SELECT max(ID) FROM PUSH_HISTORY", nativeQuery = true)
    public Long maxIndex();
}
