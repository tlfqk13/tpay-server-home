package com.tpay.domains.point_scheduled.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointScheduledRepository extends JpaRepository<PointScheduledEntity,Long> {
}
