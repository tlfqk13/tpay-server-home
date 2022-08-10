package com.tpay.domains.point.domain;

import com.tpay.domains.point.application.dto.PointTotalResponseInterface;
import com.tpay.domains.point.application.dto.WithdrawalFindNextInterface;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, Long> {

    @EntityGraph(attributePaths = {"orderEntity", "franchiseeEntity"})
    List<PointEntity> findAllByPointStatusInAndFranchiseeEntityIdAndCreatedDateBetween(
        List<PointStatus> pointStatus, Long franchiseeId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

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
    PointTotalResponseInterface findPointsTotal(@Param("franchiseeIndex") Long franchiseeIndex, @Param("disappearDate") LocalDateTime disappearDate);

    @Query(value = "select id, franchisee_id, withdrawal_check as withdrawalCheck\n" +
        "from points\n" +
        "where franchisee_id = :franchiseeIndex\n" +
        "  and withdrawal_check != 0 and point_status = 'SAVE'\n" +
        "Order by id\n" +
        "limit 0, 1", nativeQuery = true)
    WithdrawalFindNextInterface findNext(@Param("franchiseeIndex") Long franchiseeIndex);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<PointEntity> findByPointStatusInAndIsReadInOrderByIdDesc(@Param("pointStatus") List<PointStatus> pointStatus, @Param("isRead") List<Boolean> isRead, Pageable pageable);
    //Page<PointEntity> findByPointStatusInAndIsReadInOrderByIdDesc(@Param("pointStatus") List<PointStatus> pointStatus, @Param("isRead") List<Boolean> isRead, Pageable pageable);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<PointEntity> findByPointStatusInAndIsReadInAndFranchiseeEntityBusinessNumberContainingOrderByIdDesc(
            @Param("pointStatus") List<PointStatus> pointStatus, @Param("isRead") List<Boolean> isRead
            ,Pageable pageable, @Param("businessNumber") String businessNumber);
    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<PointEntity> findByPointStatusInAndIsReadInAndFranchiseeEntityStoreNameContainingOrderByIdDesc(
            @Param("pointStatus") List<PointStatus> pointStatus, @Param("isRead") List<Boolean> isRead
            ,Pageable pageable, @Param("storeName") String storeName);

    @NotNull
    @Override
    Optional<PointEntity> findById(@NotNull Long pointsIndex);

    List<PointEntity> findByCreatedDateBefore(@Param("disappearDate") LocalDateTime disappearDate);

}
