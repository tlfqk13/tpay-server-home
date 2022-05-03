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

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
    @Query(
        value =
            "select r.id           as refundIndex,\n" +
                "       purchs_sn      as orderNumber,\n" +
                "       r.created_date as createdDate,\n" +
                "       tot_amt        as totalAmount,\n" +
                "       tot_refund     as totalRefund,\n" +
                "       refund_status  as refundStatus\n" +
                "from refund r left join orders o on r.order_id = o.id\n" +
                "where o.franchisee_id = :franchiseeIndex and r.created_date between :startDate and :endDate \n" +
                "order by r.created_date desc",
        nativeQuery = true)
    List<RefundFindResponseInterface> findAllByFranchiseeIndex(
        @Param("franchiseeIndex") Long franchiseeIndex,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);


    List<RefundEntity> findAllByIdAndCreatedDateBetween(Long refundIndex, LocalDateTime startDate, LocalDateTime endDate);

    Optional<RefundEntity> findByOrderEntity(OrderEntity orderEntity);

    @Query(
        value =
            "select * from refund r right join orders o on r.order_id = o.id left join customer c on o.customer_id = c.id where o.franchisee_id = :franchiseeIndex and c.cus_pass_no = :passportNumber and r.created_date between :startDate and :endDate",
        nativeQuery = true)
    List<RefundEntity> findAllByPassportNumber(
        @Param("franchiseeIndex") Long franchiseeIndex,
        @Param("passportNumber") String passportNumber,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query(value =
        "select r.id            as refundIndex,\n" +
            "       o.purchs_sn     as orderNumber,\n" +
            "       r.created_date  as createdDate,\n" +
            "       o.tot_amt       as totalAmount,\n" +
            "       r.tot_refund    as totalRefund,\n" +
            "       o.tot_amt - r.tot_refund    as actualAmount,\n" +
            "       r.refund_status as refundStatus,\n" +
            "       f.biz_no        as businessNumber,\n" +
            "       f.store_nm      as storeName\n" +
            "from refund r\n" +
            "         inner join orders o on r.order_id = o.id\n" +
            "         left join franchisee f on o.franchisee_id = f.id\n" +
            "where r.created_date between :startLocalDate and :endLocalDate\n" +
            "order by refundIndex desc", nativeQuery = true)
    List<RefundFindResponseInterface> findAllNativeQuery(@Param("startLocalDate") LocalDate startLocalDate, @Param("endLocalDate") LocalDate endLocalDate);

    @Query(value =
        "select r.id            as refundIndex,\n" +
            "       o.purchs_sn     as orderNumber,\n" +
            "       r.created_date  as createdDate,\n" +
            "       o.tot_amt       as totalAmount,\n" +
            "       r.tot_refund    as totalRefund,\n" +
            "       o.tot_amt - r.tot_refund    as actualAmount,\n" +
            "       r.refund_status as refundStatus,\n" +
            "       f.biz_no        as businessNumber,\n" +
            "       f.store_nm      as storeName\n" +
            "from refund r\n" +
            "         inner join orders o on r.order_id = o.id\n" +
            "         left join franchisee f on o.franchisee_id = f.id\n" +
            "where r.created_date between :startLocalDate and :endLocalDate\n" +
            "and refund_status = :ordinal\n" +
            "order by refundIndex desc", nativeQuery = true)
    List<RefundFindResponseInterface> findRefundStatusNativeQuery(@Param("startLocalDate") LocalDate startLocalDate, @Param("endLocalDate") LocalDate endLocalDate, @Param("ordinal") Integer ordinal);

    @Query(value = "select\n" +
        "       r.id             as refundIndex,\n" +
        "       o.purchs_sn      as orderNumber,\n" +
        "       r.created_date   as createdDate,\n" +
        "       o.tot_amt        as totalAmount,\n" +
        "       r.tot_refund     as totalRefund,\n" +
        "       o.tot_amt - r.tot_refund    as actualAmount,\n" +
        "       r.refund_status  as refundStatus,\n" +
        "       f.biz_no         as businessNumber,\n" +
        "       f.store_nm       as storeName\n" +
        "    from refund r inner join orders o on r.order_id = o.id\n" +
        "                  left join franchisee f on o.franchisee_id = f.id\n" +
        "    where f.id = :franchiseeIndex\n" +
        "    order by refundIndex desc;", nativeQuery = true)
    List<RefundFindResponseInterface> findAFranchiseeNativeQuery(Long franchiseeIndex);

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
        "       purchs_sn      as orderNumber,\n" +
        "       r.created_date as createdDate,\n" +
        "       tot_amt        as totalAmount,\n" +
        "       tot_refund     as totalRefund,\n" +
        "       refund_status  as refundStatus\n" +
        "from refund r\n" +
        "         left join orders o on o.id = r.order_id\n" +
        "         left join customer c on c.id = o.customer_id\n" +
        "         left join franchisee f on o.franchisee_id = f.id\n" +
        "where franchisee_id = :franchiseeIndex\n" +
        "  and r.created_date between :startDate and :endDate\n" +
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
