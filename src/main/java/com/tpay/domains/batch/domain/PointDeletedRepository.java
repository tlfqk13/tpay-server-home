package com.tpay.domains.batch.domain;

import com.tpay.domains.point.domain.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointDeletedRepository extends JpaRepository<PointEntity,Long> {
}
