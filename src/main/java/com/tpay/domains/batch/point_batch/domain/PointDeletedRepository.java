package com.tpay.domains.batch.point_batch.domain;

import com.tpay.domains.point.domain.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointDeletedRepository extends JpaRepository<PointDeletedEntity, Long> {
}
