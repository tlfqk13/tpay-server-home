package com.tpay.domains.point_scheduled.domain;

import com.tpay.domains.point.application.dto.StatusUpdateResponseInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PointScheduledRepository extends JpaRepository<PointScheduledEntity,Long> {


  @Query(value = "select id, order_id\n" +
      "                from point_scheduled\n" +
      "                where created_date <= :scheduledDate\n" +
      "                    and point_status = 'SCHEDULED'", nativeQuery = true)
  Optional<List<StatusUpdateResponseInterface>> findNeedUpdateEntity(@Param("scheduledDate") LocalDate scheduledDate);

}
