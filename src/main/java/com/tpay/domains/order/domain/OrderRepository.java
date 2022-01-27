package com.tpay.domains.order.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tpay.domains.franchisee.application.dto.vat.FranchiseeVatDetailResponseInterface;
import com.tpay.domains.franchisee.application.dto.vat.FranchiseeVatReportResponseInterface;
import com.tpay.domains.franchisee.application.dto.vat.FranchiseeVatTotalResponseInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
  Optional<List<OrderEntity>> findAllByFranchiseeEntityId(Long franchiseeId);

  @Query(value = "select  franchisee_id as franchiseeIndex\n" +
      "        ,sum( cast(tot_amt as INTEGER )) as totalAmount\n" +
      "        ,count(*) as totalCount\n" +
      "    from orders o inner join refund r on o.id = r.order_id\n" +
      "    where refund_status = 'APPROVAL'\n" +
      "    and o.created_date between :startDate and :endDate\n" +
      "    and franchisee_id = :franchiseeIndex\n" +
      "    group by franchisee_id;",nativeQuery = true)
  FranchiseeVatReportResponseInterface findQuarterlyVatReport(@Param("franchiseeIndex") Long franchiseeIndex, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);



  @Query(value = "select sum(cast(tot_amt as INTEGER)) as totalAmount\n" +
      "    from orders o inner join refund r on o.id = r.order_id\n" +
      "    where refund_status = 'APPROVAL'\n" +
      "    and franchisee_id = :franchiseeIndex",nativeQuery = true)
  Optional<Long> sumTotalSaleAmountByFranchiseeIndex(@Param("franchiseeIndex") Long franchiseeIndex);



  @Query(value = "select\n" +
      "    count(*) as totalCount\n" +
      "    ,sum( cast ( tot_amt  as INTEGER )) as totalAmount\n" +
      "    ,sum( cast ( tot_vat  as INTEGER )) as totalVat\n" +
      "    from orders o inner join refund r on o.id = r.order_id\n" +
      "    where franchisee_id = :franchiseeIndex\n" +
      "    and o.created_date between :startDate and :endDate",nativeQuery = true)
  FranchiseeVatTotalResponseInterface findQuarterlyTotal(@Param("franchiseeIndex") Long franchiseeIndex, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


  @Query(value = "select\n" +
      "    purchs_sn                                as purchaseSerialNumber\n" +
      "    ,substr(replace(o.created_date,'-',''),1,8) as saleDate\n" +
      "    ,tk_out_conf_no                             as takeoutConfirmNumber\n" +
      "    ,tot_refund                                 as refundAmount\n" +
      "    ,tot_amt                                    as amount\n" +
      "    ,tot_vat                                    as vat\n" +
      "    from orders o inner join refund r on o.id = r.order_id\n" +
      "    where franchisee_id = :franchiseeIndex\n" +
      "    and o.created_date between :startDate and :endDate\n" +
      "    order by 3 desc", nativeQuery = true)
  List<FranchiseeVatDetailResponseInterface> findQuarterlyVatDetail(@Param("franchiseeIndex") Long franchiseeIndex, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
