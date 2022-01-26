package com.tpay.domains.order.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tpay.domains.franchisee.application.dto.FranchiseeVatReportResponseInterface;
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
  Long sumTotalSaleAmountByFranchiseeIndex(@Param("franchiseeIndex") Long franchiseeIndex);
}
