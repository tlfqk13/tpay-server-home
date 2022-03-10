package com.tpay.domains.point.domain;

import com.tpay.domains.point.application.dto.PointTotalResponseInterface;
import com.tpay.domains.point.application.dto.StatusUpdateResponseInterface;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<PointEntity, Long> {
  List<PointEntity> findAllByFranchiseeEntityIdAndCreatedDateBetween(
      Long franchiseeId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

  @Query(value = "select cast(sum(if(created_date > :scheduledDate, change_value, 0)) as integer)                          as scheduledPoint,\n" +
      "       cast(sum(if(created_date < :disappearDate, change_value, 0)) as integer)                          as disappearPoint,\n" +
      "       cast(sum(if(created_date between :disappearDate and :scheduledDate, change_value, 0)) as integer) as totalPoint,\n" +
      "       franchisee_status as franchiseeStatus\n" +
      "from (select *\n" +
      "      from points\n" +
      "      where order_id not in (select distinct order_id from points where point_status = 'CANCEL')) as points\n" +
      "         left join (select franchisee_id as fi, franchisee_status from franchisee_applicant) as fa\n" +
      "                   on points.franchisee_id = fa.fi\n" +
      "where franchisee_id = :franchiseeIndex", nativeQuery = true)
  PointTotalResponseInterface findPointsTotal(@Param("franchiseeIndex") Long franchiseeIndex, @Param("disappearDate") LocalDate disappearDate, @Param("scheduledDate") LocalDate scheduledDate);


  @Query(value = "select id\n" +
      "    from points\n" +
      "    where franchisee_id = :franchiseeIndex\n" +
      "        and created_date <= :scheduledDate\n" +
      "        and point_status = 'SCHEDULED'", nativeQuery = true)
  Optional<List<StatusUpdateResponseInterface>> findNeedUpdateEntity(Long franchiseeIndex, LocalDate scheduledDate);

}
