package com.tpay.domains.push.domain;

import com.tpay.domains.push.application.dto.AdminPushDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PushHistoryRepository extends JpaRepository<PushHistoryEntity, Long> {
    Optional<List<PushHistoryEntity>> findByUserIdOrderByIdDesc(Long userId);

    long countByUserIdAndIsRead(Long userId, Boolean isRead);

    @Query(value = "select id,created_date as createdDate,title from push_history\n" +
        "where push_category =15\n" +
        "group by date(created_date), title, body order by 1 desc", nativeQuery = true)
    List<AdminPushDto> findAllAnnouncement();
}
