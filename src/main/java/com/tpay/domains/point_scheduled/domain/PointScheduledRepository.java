package com.tpay.domains.point_scheduled.domain;

import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.PointStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PointScheduledRepository extends JpaRepository<PointScheduledEntity, Long> {

    @EntityGraph(attributePaths = {"orderEntity", "franchiseeEntity"})
    List<PointScheduledEntity> findAllByFranchiseeEntityIdAndCreatedDateBetween(
        Long franchiseeId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    List<PointScheduledEntity> findByCreatedDateBeforeAndPointStatus(LocalDateTime localDateTime, PointStatus pointStatus);

    Optional<PointScheduledEntity> findByOrderEntity(OrderEntity orderEntity);
    void deleteByFranchiseeEntityId(Long franchiseeIndex);
}
