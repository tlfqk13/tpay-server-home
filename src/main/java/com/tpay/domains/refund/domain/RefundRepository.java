package com.tpay.domains.refund.domain;

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
}
