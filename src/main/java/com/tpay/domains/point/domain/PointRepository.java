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

  @Query(value = "select psf.*, dis.disappearPoint\n" +
      "from (\n" +
      "         select franchisee_id\n" +
      "              , scheduledPoint\n" +
      "              , balance as TotalPoint\n" +
      "              , franchisee_status\n" +
      "         from (select franchisee_id\n" +
      "                    , cast(sum(if(point_status = 'SCHEDULED', value, 0)) as integer) as scheduledPoint\n" +
      "               from point_scheduled\n" +
      "               where franchisee_id = :franchiseeIndex) ps\n" +
      "                  left join (select f.id, f.balance, fa.franchisee_status\n" +
      "                             from franchisee f\n" +
      "                                      left join franchisee_applicant fa on f.id = fa.franchisee_id) f\n" +
      "                            on ps.franchisee_id = f.id\n" +
      "     ) psf\n" +
      "         left join\n" +
      "     (\n" +
      "         select franchisee_id, sum(withdrawal_check) as disappearPoint\n" +
      "         from points\n" +
      "         where created_date < :disappearDate\n" +
      "           and franchisee_id = :franchiseeIndex\n" +
      "     ) dis on psf.franchisee_id = dis.franchisee_id", nativeQuery = true)
  PointTotalResponseInterface findPointsTotal(@Param("franchiseeIndex") Long franchiseeIndex, @Param("disappearDate") LocalDate disappearDate);


  @Query(value = "select id\n" +
      "    from points\n" +
      "    where franchisee_id = :franchiseeIndex\n" +
      "        and created_date <= :scheduledDate\n" +
      "        and point_status = 'SCHEDULED'", nativeQuery = true)
  Optional<List<StatusUpdateResponseInterface>> findNeedUpdateEntity(Long franchiseeIndex, LocalDate scheduledDate);

}
