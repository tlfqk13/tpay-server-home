package com.tpay.domains.notice.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    List<NoticeEntity> findByIsFixedAndScheduledDateBeforeAndIsInvisible(Boolean isFixed, LocalDateTime localDateTime,Boolean aFalse);

    List<NoticeEntity> findByScheduledDateBeforeAndIsInvisible(LocalDateTime localDateTime, Boolean aFalse);

    List<NoticeEntity> findAllByOrderByIdDesc(Pageable pageable);
}
