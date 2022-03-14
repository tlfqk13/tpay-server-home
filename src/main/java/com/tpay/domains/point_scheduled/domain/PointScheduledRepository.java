package com.tpay.domains.point_scheduled.domain;

import com.tpay.domains.point.application.dto.StatusUpdateResponseInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PointScheduledRepository extends JpaRepository<PointScheduledEntity,Long> {


  @Query(value = "select id, order_id\n" +
      "          from point_scheduled\n" +
      "          where franchisee_id = :franchiseeIndex\n" +
      "              and created_date <= :scheduledDate\n" +
      "              and point_status = 'SCHEDULED'\n", nativeQuery = true)
  Optional<List<StatusUpdateResponseInterface>> findNeedUpdateEntity(Long franchiseeIndex, LocalDate scheduledDate);

}
