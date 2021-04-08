package com.tpay.domains.point.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<PointEntity, Long> {
  List<PointEntity> findAllByFranchiseeEntityId(Long franchiseeId);
}
