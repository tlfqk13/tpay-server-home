package com.tpay.domains.point.domain;

import com.tpay.domains.point.application.dto.PointTotalResponseInterface;
import com.tpay.domains.point.application.dto.StatusUpdateResponseInterface;
import com.tpay.domains.point.application.dto.WithdrawalFindNextInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<PointEntity, Long> {

  @Query(value = "select psf.*, ifNull(dis.disappearPoint,0) as disappearPoint\n" +
      "from (\n" +
      "         select franchisee_id\n" +
      "              , scheduledPoint\n" +
      "              , balance as TotalPoint\n" +
      "              , franchisee_status as franchiseeStatus\n" +
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


  @Query(value = "select id, franchisee_id, withdrawal_check as withdrawalCheck\n" +
      "from points\n" +
      "where franchisee_id = :franchiseeIndex\n" +
      "  and withdrawal_check != 0 and point_status = 'SAVE'\n" +
      "Order by id\n" +
      "limit 0, 1", nativeQuery = true)
  WithdrawalFindNextInterface findNext(@Param("franchiseeIndex") Long franchiseeIndex);
}
