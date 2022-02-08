package com.tpay.domains.refund.domain;

import com.tpay.domains.refund.application.dto.RefundFindResponseInterface;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
  @Query(
      value =
          "select * from refund r left join orders o on r.order_id = o.id where o.franchisee_id = :franchiseeIndex and r.created_date between :startDate and :endDate",
      nativeQuery = true)
  List<RefundEntity> findAllByFranchiseeIndex(
      @Param("franchiseeIndex") Long franchiseeIndex,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

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
      "select\n" +
          "       r.id             as refundIndex,\n" +
          "       o.purchs_sn      as orderNumber,\n" +
          "       r.created_date   as createdDate,\n" +
          "       o.tot_amt        as totalAmount,\n" +
          "       r.tot_refund     as totalRefund,\n" +
          "       r.refund_status  as refundStatus,\n" +
          "       f.biz_no         as businessNumber,\n" +
          "       f.store_nm       as storeName\n" +
          "    from refund r inner join orders o on r.order_id = o.id\n" +
          "                  left join franchisee f on o.franchisee_id = f.id\n" +
          "    order by refundIndex desc;", nativeQuery = true)
  List<RefundFindResponseInterface> findAllNativeQuery();


  @Query(value = "select\n" +
      "       r.id             as refundIndex,\n" +
      "       o.purchs_sn      as orderNumber,\n" +
      "       r.created_date   as createdDate,\n" +
      "       o.tot_amt        as totalAmount,\n" +
      "       r.tot_refund     as totalRefund,\n" +
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
          "select *,\n" +
              "       (results.totalAmount - results.totalRefund) as actualAmount\n" +
              "from (select date(o.created_date)                                                      as date,\n" +
              "             cast(sum(if(r.refund_status = 'APPROVAL', o.tot_amt, 0)) as integer)      as totalAmount,\n" +
              "             cast(sum(if(r.refund_status = 'APPROVAL', o.tot_vat, 0)) as integer)      as totalVat,\n" +
              "             cast(sum(if(r.refund_status = 'APPROVAL', r.tot_refund, 0)) as integer)   as totalRefund,\n" +
              "             cast(sum(if(r.refund_status = 'APPROVAL', p.change_value, 0)) as integer) as totalPoint,\n" +
              "             sum(if(r.refund_status = 'APPROVAL', 1, 0))                               as saleCount,\n" +
              "             sum(if(r.refund_status = 'CANCEL', 1, 0))                                 as cancelCount\n" +
              "      from orders o\n" +
              "               left join refund r\n" +
              "                         on o.id = r.order_id\n" +
              "               left join points p on o.id = p.order_id\n" +
              "      where o.franchisee_id = :franchiseeIndex\n" +
              "        and p.point_status = 'SAVE'\n" +
              "        and o.created_date between :startDate\n" +
              "          and :endDate\n" +
              "      group by date(o.sale_datm)) as results\n" +
              "order by date desc;",
      nativeQuery = true)
  List<SaleAnalysisFindResponse> findSaleAnalysis(
      @Param("franchiseeIndex") Long franchiseeIndex,
      @Param("startDate") LocalDateTime startDateTime,
      @Param("endDate") LocalDateTime endDateTime);


  @Query(
      value =
          "select *, (results.totalAmount - results.totalRefund) as actualAmount\n" +
              "              from (select date(o.created_date)                                                      as date,\n" +
              "                           cast(sum(if(r.refund_status = 'APPROVAL', o.tot_amt, 0)) as integer)      as totalAmount,\n" +
              "                           cast(sum(if(r.refund_status = 'APPROVAL', o.tot_vat, 0)) as integer)      as totalVat,\n" +
              "                           cast(sum(if(r.refund_status = 'APPROVAL', r.tot_refund, 0)) as integer)   as totalRefund,\n" +
              "                           cast(sum(if(r.refund_status = 'APPROVAL', p.change_value, 0)) as integer) as totalPoint,\n" +
              "                           sum(if(r.refund_status = 'APPROVAL', 1, 0))                               as saleCount,\n" +
              "                           sum(if(r.refund_status = 'CANCEL', 1, 0))                                 as cancelCount\n" +
              "                    from orders o\n" +
              "                             left join refund r\n" +
              "                                       on o.id = r.order_id\n" +
              "                             left join points p on o.id = p.order_id\n" +
              "                    where o.franchisee_id = :franchiseeIndex\n" +
              "                      and p.point_status = 'SAVE'\n" +
              "                      and replace(date(o.created_date),'-','') between :startDate and :endDate\n" +
              "                    group by date(o.sale_datm)) as results\n" +
              "              order by date desc;", nativeQuery = true)
  List<SaleAnalysisFindResponse> findSaleAnalysisV2(
      @Param("franchiseeIndex") Long franchiseeIndex,
      @Param("startDate") String startDate,
      @Param("endDate") String endDate);


  @Query(value ="select distinct substr(o.created_date, 1, 10) as date\n" +
      "from orders o\n" +
      "         left join refund r\n" +
      "                   on o.id = r.order_id\n" +
      "         left join points p on o.id = p.order_id\n" +
      "where o.franchisee_id = :franchiseeIndex\n" +
      "  and replace(substr(o.created_date,1,10), '-', '') between :startDate and :endDate",nativeQuery = true)
  List<Object> dateReturnTest(
      @Param("franchiseeIndex") Long franchiseeIndex,
      @Param("startDate") String startDate,
      @Param("endDate") String endDate);
}
