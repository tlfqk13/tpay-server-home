package com.tpay.domains.refund.domain;

import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.application.dto.RefundFindResponseInterface;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponseInterface;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponseInterface;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<RefundEntity, Long>, RefundRepositoryCustom {
    @Query(
            value =
                    "select r.id           as refundIndex,\n" +
                            "       purchs_sn      as orderNumber,\n" +
                            "       r.created_date as createdDate,\n" +
                            "       tot_amt        as totalAmount,\n" +
                            "       tot_refund     as totalRefund,\n" +
                            "       refund_status  as refundStatus,\n" +
                            "       p.value      as point\n" +
                            "from refund r\n" +
                            "left join orders o on r.order_id = o.id\n" +
                            "left join point_scheduled p on o.id = p.order_id\n" +
                            "where o.franchisee_id = :franchiseeIndex and r.created_date between :startDate and :endDate \n" +
                            "group by r.id desc",
            nativeQuery = true)
    List<RefundFindResponseInterface> findAllByFranchiseeIndex(
            @Param("franchiseeIndex") Long franchiseeIndex,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    List<RefundEntity> findAllByIdAndCreatedDateBetween(Long refundIndex, LocalDateTime startDate, LocalDateTime endDate);

    Optional<RefundEntity> findByOrderEntity(OrderEntity orderEntity);

    @Query(
            value =
                    "select date(o.created_date)                                                                   as formatDate\n" +
                            "     , cast(sum(if(r.refund_status = 0, tot_amt, 0)) as integer)                      as totalAmount\n" +
                            "     , cast(sum(if(r.refund_status = 0, tot_refund, 0)) as integer)                   as totalRefund\n" +
                            "     , cast(sum(if(r.refund_status = 0, tot_amt, 0)) - sum(if(r.refund_status = 0, tot_refund, 0)) as integer) as actualAmount\n" +
                            "     , sum(if(r.refund_status = 0, 1, 0))                                                     as saleCount\n" +
                            "     , sum(if(r.refund_status = 2, 1, 0))                                                     as cancelCount\n" +
                            "from orders o\n" +
                            "         left join refund r on o.id = r.order_id\n" +
                            "where o.franchisee_id = :franchiseeIndex\n" +
                            "  and r.created_date between :startDate and :endDate\n" +
                            "order by 1 desc",
            nativeQuery = true)
    RefundFindResponseInterface findRefundDetailSaleTotalQuery(
            @Param("franchiseeIndex") Long franchiseeIndex,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(
            value =
                    "select date(o.created_date)                                                                   as formatDate\n" +
                            "     , sum(if(r.refund_status = 0, tot_amt, 0))                                               as totalAmount\n" +
                            "     , sum(if(r.refund_status = 0, tot_refund, 0))                                            as totalRefund\n" +
                            "     , sum(if(r.refund_status = 0, tot_amt, 0)) - sum(if(r.refund_status = 0, tot_refund, 0)) as actualAmount\n" +
                            "     , sum(if(r.refund_status = 0, 1, 0))                                                     as saleCount\n" +
                            "     , sum(if(r.refund_status = 2, 1, 0))                                                     as cancelCount\n" +
                            "from orders o\n" +
                            "         left join refund r on o.id = r.order_id\n" +
                            "where o.franchisee_id = :franchiseeIndex\n" +
                            "  and r.created_date between :startDate and :endDate\n" +
                            "group by date(o.created_date)\n" +
                            "order by 1 desc",
            nativeQuery = true)
    List<SaleAnalysisFindResponseInterface> findSaleAnalysis(
            @Param("franchiseeIndex") Long franchiseeIndex,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "select r.id           as refundIndex,\n" +
            "       purchs_sn       as orderNumber,\n" +
            "       r.created_date  as createdDate,\n" +
            "  date(r.created_date) as formatDate,\n" +
            "       tot_amt         as totalAmount,\n" +
            "       tot_refund      as totalRefund,\n" +
            "       refund_status   as refundStatus,\n" +
            "       p.value   as point\n" +
            "from refund r\n" +
            "         left join orders o on o.id = r.order_id\n" +
            "         left join customer c on c.id = o.customer_id\n" +
            "         left join point_scheduled p on o.id = p.order_id\n" +
            "         left join franchisee f on o.franchisee_id = f.id\n" +
            "where o.franchisee_id = :franchiseeIndex\n" +
            "  and r.created_date between :startDate and :endDate\n" +
            "  and r.refund_status = 0\n" +
            "  and customer_id = :customerIndex\n" +
            "order by 3", nativeQuery = true)
    List<RefundFindResponseInterface> findRefundsByCustomerInfo(
            @Param("franchiseeIndex") Long franchiseeIndex,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("customerIndex") Long customerIndex);

    @Query(value =
            "select cast(sum(if(r.refund_status = 0, o.tot_amt, 0)) as integer)      as totalAmount,\n" +
                    "       (cast(sum(if(r.refund_status = 0, o.tot_amt, 0)) as integer)) -\n" +
                    "       (cast(sum(if(r.refund_status = 0, r.tot_refund, 0)) as integer)) as totalActualAmount,\n" +
                    "       cast(sum(if(r.refund_status = 0, r.tot_refund, 0)) as integer)   as totalRefund,\n" +
                    "       cast(sum(if(r.refund_status = 0, 1, 0)) as integer)              as totalCount,\n" +
                    "       sum(if(r.refund_status = 2, 1, 0))                                 as totalCancel\n" +
                    "from orders o\n" +
                    "         left join refund r on o.id = r.order_id\n" +
                    "where franchisee_id = :franchiseeIndex\n" +
                    "  and substr(replace(o.created_date, '-', ''), 1, 6) = :targetDate", nativeQuery = true)
    SaleStatisticsResponseInterface findMonthStatistics(
            @Param("franchiseeIndex") Long franchiseeIndex,
            @Param("targetDate") String targetDate
    );

    @Query(value =
            "select cast(sum(if(r.refund_status = 0, o.tot_amt, 0)) as integer)      as totalAmount,\n" +
                    "       (cast(sum(if(r.refund_status = 0, o.tot_amt, 0)) as integer)) -\n" +
                    "       (cast(sum(if(r.refund_status = 0, r.tot_refund, 0)) as integer)) as totalActualAmount,\n" +
                    "       cast(sum(if(r.refund_status = 0, r.tot_refund, 0)) as integer)   as totalRefund,\n" +
                    "       cast(sum(if(r.refund_status = 0, 1, 0)) as integer)              as totalCount,\n" +
                    "       sum(if(r.refund_status = 2, 1, 0))                                 as totalCancel\n" +
                    "from orders o\n" +
                    "         left join refund r on o.id = r.order_id\n" +
                    "where franchisee_id = :franchiseeIndex\n" +
                    "  and substr(replace(o.created_date, '-', ''), 1, 4) = :targetDate", nativeQuery = true)
    SaleStatisticsResponseInterface findYearStatistics(
            @Param("franchiseeIndex") Long franchiseeIndex,
            @Param("targetDate") String targetDate
    );


    @Query(value =
            "select cast(sum(if(r.refund_status = 0, o.tot_amt, 0)) as integer)      as totalAmount,\n" +
                    "       (cast(sum(if(r.refund_status = 0, o.tot_amt, 0)) as integer)) -\n" +
                    "       (cast(sum(if(r.refund_status = 0, r.tot_refund, 0)) as integer)) as totalActualAmount,\n" +
                    "       cast(sum(if(r.refund_status = 0, r.tot_refund, 0)) as integer)   as totalRefund,\n" +
                    "       cast(sum(if(r.refund_status = 0, 1, 0)) as integer)              as totalCount,\n" +
                    "       sum(if(r.refund_status = 2, 1, 0))                                 as totalCancel\n" +
                    "from orders o\n" +
                    "         left join refund r on o.id = r.order_id\n" +
                    "where franchisee_id = :franchiseeIndex", nativeQuery = true)
    SaleStatisticsResponseInterface findAllStatistics(
            @Param("franchiseeIndex") Long franchiseeIndex
    );

    @EntityGraph(attributePaths = {"orderEntity"})
    List<RefundEntity> findByCreatedDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
