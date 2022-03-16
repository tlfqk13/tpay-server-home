package com.tpay.domains.point.domain;

import com.tpay.domains.point.application.dto.AdminPointFindResponseInterface;
import com.tpay.domains.point.application.dto.PointTotalResponseInterface;
import com.tpay.domains.point.application.dto.StatusUpdateResponseInterface;
import com.tpay.domains.point.application.dto.WithdrawalFindNextInterface;
import com.tpay.domains.point_scheduled.domain.PointScheduledEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<PointEntity, Long> {


  List<PointEntity> findAllByFranchiseeEntityIdAndCreatedDateBetweenAndPointStatus(
      Long franchiseeId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable, PointStatus pointStatus);

  @Query(value = "select psf.*, ifNull(dis.disappearPoint, 0) as disappearPoint\n" +
      "from (\n" +
      "         select franchisee_id\n" +
      "              , ifNull(scheduledPoint, 0) as scheduledPoint\n" +
      "              , ifNull(balance, 0)        as TotalPoint\n" +
      "         from (select franchisee_id\n" +
      "                    , cast(sum(if(point_status = 'SCHEDULED', value, 0)) as integer) as scheduledPoint\n" +
      "               from point_scheduled\n" +
      "               where franchisee_id = :franchiseeIndex) ps\n" +
      "                  left join (select id, balance\n" +
      "                             from franchisee) f\n" +
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



  //이 아래는 admin 요청 관련
  // TODO: 2022/03/15 이렇게 조건별로 네이티브 쿼리를 각각 구현한 것은, 파라미터 바인딩에는 단순 값만 들어가고 조건에 따른 문장 (예를들어 조회에 따라 쿼리의 where절 자체가 바뀌는 경우 등)은 구현이 불가하다고 판단했기 때문임
  // TODO: 2022/03/15 추후 JPA로 변경 또는 네이티브 쿼리에 where절 자체를 변수로주는 방법을 찾아야함
  // TODO: 2022/03/15 성능은 각각 API를 조건별로 분기하는 것이므로, 지금처럼 하는 것이 제일 빠를 것으로 예상함
  // 포인트 출금 리스트 조회
  // 1. 둘 다 ALL
  @Query(value = "select p.id           as pointsIndex,\n" +
      "       point_status as pointStatus,\n" +
      "       biz_no         as businessNumber,\n" +
      "       store_nm       as storeName,\n" +
      "       sel_nm         as sellerName,\n" +
      "       p.created_date as requestedDate,\n" +
      "       change_value   as amount,\n" +
      "       is_read        as isRead\n" +
      "from points p\n" +
      "         left join franchisee f on p.franchisee_id = f.id\n" +
      "where point_status in ('WITHDRAW', 'COMPLETE') order by pointsIndex desc", nativeQuery = true)
  List<AdminPointFindResponseInterface> findPointsAdminAll();


  //2. 둘 중 하나만 필터
  //2-1. is_read가 false인 경우
  @Query(value = "select p.id           as pointsIndex,\n" +
      "       point_status as pointStatus,\n" +
      "       biz_no         as businessNumber,\n" +
      "       store_nm       as storeName,\n" +
      "       sel_nm         as sellerName,\n" +
      "       p.created_date as requestedDate,\n" +
      "       change_value   as amount,\n" +
      "       is_read        as isRead\n" +
      "from points p\n" +
      "         left join franchisee f on p.franchisee_id = f.id\n" +
      "where point_status in ('WITHDRAW', 'COMPLETE')\n" +
      "and is_read = false order by pointsIndex desc", nativeQuery = true)
  List<AdminPointFindResponseInterface> findPointsAdminIsReadFalse();

  //2-2. PointsStatus - Withdraw
  // TODO: 2022/03/15 status를 파라미터 바인딩으로 처리하지 않고 분기를한 이유는 Enumerated 되어있어서 숫자로 안먹힘. 추후 개선요망
  @Query(value = "select p.id           as pointsIndex,\n" +
      "       point_status as pointStatus,\n" +
      "       biz_no         as businessNumber,\n" +
      "       store_nm       as storeName,\n" +
      "       sel_nm         as sellerName,\n" +
      "       p.created_date as requestedDate,\n" +
      "       change_value   as amount,\n" +
      "       is_read        as isRead\n" +
      "from points p\n" +
      "         left join franchisee f on p.franchisee_id = f.id\n" +
      "where point_status = 'WITHDRAW' order by pointsIndex desc", nativeQuery = true)
  List<AdminPointFindResponseInterface> findPointsAdminWithdraw();

  //2-3. PointStatus - COMPLETE
  @Query(value = "select p.id           as pointsIndex,\n" +
      "       point_status as pointStatus,\n" +
      "       biz_no         as businessNumber,\n" +
      "       store_nm       as storeName,\n" +
      "       sel_nm         as sellerName,\n" +
      "       p.created_date as requestedDate,\n" +
      "       change_value   as amount,\n" +
      "       is_read        as isRead\n" +
      "from points p\n" +
      "         left join franchisee f on p.franchisee_id = f.id\n" +
      "where point_status = 'COMPLETE' order by pointsIndex desc", nativeQuery = true)
  List<AdminPointFindResponseInterface> findPointsAdminComplete();

  //2-4, 2-5 isRead False 이면서 각각 Status별 쿼리
  @Query(value = "select p.id           as pointsIndex,\n" +
      "       point_status as pointStatus,\n" +
      "       biz_no         as businessNumber,\n" +
      "       store_nm       as storeName,\n" +
      "       sel_nm         as sellerName,\n" +
      "       p.created_date as requestedDate,\n" +
      "       change_value   as amount,\n" +
      "       is_read        as isRead\n" +
      "from points p\n" +
      "    left join franchisee f on p.franchisee_id = f.id\n" +
      "where point_status = 'WITHDRAW' and is_read = false order by pointsIndex desc", nativeQuery = true)
  List<AdminPointFindResponseInterface> findPointsAdminWithdrawIsReadFalse();

  @Query(value = "select p.id           as pointsIndex,\n" +
      "       point_status as pointStatus,\n" +
      "       biz_no         as businessNumber,\n" +
      "       store_nm       as storeName,\n" +
      "       sel_nm         as sellerName,\n" +
      "       p.created_date as requestedDate,\n" +
      "       change_value   as amount,\n" +
      "       is_read        as isRead\n" +
      "from points p\n" +
      "         left join franchisee f on p.franchisee_id = f.id\n" +
      "where point_status = 'COMPLETE' and is_read = false order by pointsIndex desc", nativeQuery = true)
  List<AdminPointFindResponseInterface> findPointsAdminCompleteIsReadFalse();


  @NotNull
  @Override
  Optional<PointEntity> findById(@NotNull Long pointsIndex);
}
