package com.tpay.domains.refund.domain;

import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
          "select *, (results.totalAmount - results.totalRefund) as actualAmount\n"
              + "from (select date(o.created_date)                 as date,\n"
              + "             cast(sum(o.tot_amt) as integer)      as totalAmount,\n"
              + "             cast(sum(o.tot_vat) as integer)      as totalVat,\n"
              + "             cast(sum(r.tot_refund) as integer)   as totalRefund,\n"
              + "             cast(sum(p.change_value) as integer) as totalPoint,\n"
              + "             count(*)                             as saleCount\n"
              + "      from orders o\n"
              + "               left join refund r\n"
              + "                         on o.id = r.order_id\n"
              + "               left join points p on o.id = p.order_id\n"
              + "      where o.franchisee_id = :franchiseeIndex\n"
              + "        and p.point_status = 'SAVE'\n"
              + "        and r.refund_status = 'APPROVAL'\n"
              + "        and o.created_date between :startDate\n"
              + "        and :endDate\n"
              + "      group by date(o.sale_datm)) as results;\n",
      nativeQuery = true)
  List<SaleAnalysisFindResponse> findSaleAnalysis(
      @Param("franchiseeIndex") Long franchiseeIndex,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}
