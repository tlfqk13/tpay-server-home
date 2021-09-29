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
      "select refund from RefundEntity refund where refund.orderEntity.franchiseeEntity.id = :franchiseeIndex and refund.createdDate >= :startDate and refund.createdDate < :endDate")
  List<RefundEntity> findAllByFranchiseeIndex(
      Long franchiseeIndex, LocalDateTime startDate, LocalDateTime endDate);

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
              + "        and o.created_date >= :startDate\n"
              + "        and o.created_date\n"
              + "          < :endDate\n"
              + "        and p.point_status = 'SAVE'\n"
              + "        and r.refund_status = 'APPROVAL'\n"
              + "      group by date(o.sale_datm)) as results;\n",
      nativeQuery = true)
  List<SaleAnalysisFindResponse> findSaleAnalysis(
      @Param("franchiseeIndex") Long franchiseeIndex,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}
