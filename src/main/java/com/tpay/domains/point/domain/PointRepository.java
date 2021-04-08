package com.tpay.domains.point.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<PointEntity, Long> {
  List<PointEntity> findAllByFranchiseeEntityIdAndCreatedDateBetween(
      Long franchiseeId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
