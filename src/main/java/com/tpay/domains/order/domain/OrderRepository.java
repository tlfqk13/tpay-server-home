package com.tpay.domains.order.domain;

import com.tpay.domains.order.application.dto.CmsResponseDetailInterface;
import com.tpay.domains.order.application.dto.CmsResponseInterface;
import com.tpay.domains.vat.application.dto.VatDetailResponseInterface;
import com.tpay.domains.vat.application.dto.VatReportResponseInterface;
import com.tpay.domains.vat.application.dto.VatTotalResponseInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query(value = "select franchisee_id                           as franchiseeIndex\n" +
        "     , sum(cast(tot_amt as INTEGER))           as totalAmount\n" +
//      "     , cast(sum(tot_amt - tot_vat) as INTEGER) as totalSupply\n" +
        "     , (sum(cast(tot_amt as INTEGER)) - sum(cast(tot_vat as INTEGER))) as totalSupply\n" +
        "     , sum(cast(tot_vat as INTEGER))           as totalVat\n" +
        "     , count(*)                                as totalCount\n" +
        "from orders o\n" +
        "         inner join refund r on o.id = r.order_id\n" +
        "where refund_status = 'APPROVAL'\n" +
        "  and o.created_date between :startDate and :endDate\n" +
        "  and franchisee_id = :franchiseeIndex\n" +
        "group by franchisee_id", nativeQuery = true)
    VatReportResponseInterface findQuarterlyVatReport(@Param("franchiseeIndex") Long franchiseeIndex, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query(value = "select sum(cast(tot_amt as INTEGER)) as totalAmount\n" +
        "    from orders o inner join refund r on o.id = r.order_id\n" +
        "    where refund_status = 'APPROVAL'\n" +
        "    and franchisee_id = :franchiseeIndex", nativeQuery = true)
    Optional<Long> sumTotalSaleAmountByFranchiseeIndex(@Param("franchiseeIndex") Long franchiseeIndex);


    @Query(value = "select\n" +
        "      count(*) as totalCount\n" +
        "      ,IFNULL(sum( cast ( tot_amt  as INTEGER )),0) as totalAmount\n" +
        "      ,IFNULL(sum( cast ( tot_vat  as INTEGER )),0) as totalVat\n" +
        "      ,IFNULL(sum( cast (tot_refund as INTEGER)),0) as totalRefund\n" +
        "      from orders o inner join refund r on o.id = r.order_id\n" +
        "      where franchisee_id = :franchiseeIndex\n" +
        "      and refund_status = 'APPROVAL' and o.created_date between :startDate and :endDate", nativeQuery = true)
    VatTotalResponseInterface findQuarterlyTotal(@Param("franchiseeIndex") Long franchiseeIndex, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select\n" +
            "      count(*) as totalCount\n" +
            "      ,IFNULL(sum( cast ( tot_amt  as INTEGER )),0) as totalAmount\n" +
            "      ,IFNULL(sum( cast ( tot_vat  as INTEGER )),0) as totalVat\n" +
            "      ,IFNULL(sum( cast (tot_refund as INTEGER)),0) as totalRefund\n" +
            "      from orders o inner join refund r on o.id = r.order_id\n" +
            "      where franchisee_id = :franchiseeIndex\n" +
            "      and refund_status = 'APPROVAL' and substr(o.created_date,1,4) = :year\n" +
            "      and substr(o.created_date,6,2) = :month", nativeQuery = true)
    VatTotalResponseInterface findMonthlyTotal(Long franchiseeIndex, String year, String month);

    @Query(value = "select\n" +
            "    purchs_sn                                as purchaseSerialNumber\n" +
            "    ,substr(replace(o.created_date,'-',''),1,8) as saleDate\n" +
            "    ,tk_out_conf_no                             as takeoutConfirmNumber\n" +
            "    ,tot_refund                                 as refundAmount\n" +
            "    ,tot_amt                                    as amount\n" +
            "    ,tot_vat                                    as vat\n" +
            "    ,c.cus_nm                                    as customerName\n" +
            "    ,c.cus_natn                                    as customerNational\n" +
            "    from orders o inner join refund r on o.id = r.order_id\n" +
            "                  left join customer c on c.id = o.customer_id\n" +
            "    where franchisee_id = :franchiseeIndex\n" +
            "    and refund_status = 'APPROVAL' and o.created_date between :startDate and :endDate\n" +
            "    order by 3 desc", nativeQuery = true)
    List<VatDetailResponseInterface> findQuarterlyVatDetail(@Param("franchiseeIndex") Long franchiseeIndex, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select\n" +
            "    purchs_sn                                as purchaseSerialNumber\n" +
            "    ,substr(replace(o.created_date,'-',''),1,8) as saleDate\n" +
            "    ,tk_out_conf_no                             as takeoutConfirmNumber\n" +
            "    ,tot_refund                                 as refundAmount\n" +
            "    ,tot_amt                                    as amount\n" +
            "    ,tot_vat                                    as vat\n" +
            "    ,c.cus_nm                                    as customerName\n" +
            "    ,c.cus_natn                                    as customerNational\n" +
            "    from orders o inner join refund r on o.id = r.order_id\n" +
            "                  left join customer c on c.id = o.customer_id\n" +
            "    where franchisee_id = :franchiseeIndex\n" +
            "    and refund_status = 'APPROVAL'\n" +
            "    and substr(o.created_date,1,4) = :year\n" +
            "    and substr(o.created_date,6,2) = :month\n" +
            "    order by 3 desc", nativeQuery = true)
    List<VatDetailResponseInterface> findMonthlyVatDetail(@Param("franchiseeIndex") Long franchiseeIndex, @Param("year") String year, @Param("month") String month);


    @Query(value = "select\n" +
        "    franchisee_id as franchiseeIndex,\n" +
        "    count(*) as totalCount,\n" +
        "    sum(cast( tot_amt as INTEGER )) as totalAmount,\n" +
        "    sum(cast( tot_vat as INTEGER )) as totalVat,\n" +
        "    sum(cast( tot_vat - tot_refund as INTEGER )) as totalCommission\n" +
        "    from orders o inner join refund r on o.id = r.order_id\n" +
        "where franchisee_id = :franchiseeIndex\n" +
        "and substr(o.created_date,1,4) = :year\n" +
        "and substr(o.created_date,6,2) = :month and refund_status = 'APPROVAL'\n" +
        "group by franchisee_id", nativeQuery = true)
    CmsResponseInterface findMonthlyCmsReport(@Param("franchiseeIndex") Long franchiseeIndex, @Param("year") String year, @Param("month") String month);

    @Query(value = "select franchiseeIndex,\n" +
        "       totalCount,\n" +
        "       totalAmount,\n" +
        "       totalVat,\n" +
        "       totalCommission,\n" +
        "       sellerName,\n" +
        "       bank_name       as bankName,\n" +
        "       account_number  as accountNumber,\n" +
        "       withdrawal_date as withdrawalDate,\n" +
        "       totalCommission as totalBill\n" +
        "from (select franchisee_id                              as franchiseeIndex,\n" +
        "             count(*)                                   as totalCount,\n" +
        "             sum(cast(tot_amt as INTEGER))              as totalAmount,\n" +
        "             sum(cast(tot_vat as INTEGER))              as totalVat,\n" +
        "             sum(cast(tot_vat - tot_refund as INTEGER)) as totalCommission\n" +
        "      from orders o\n" +
        "               inner join refund r on o.id = r.order_id\n" +
        "      where franchisee_id = :franchiseeIndex\n" +
        "        and substr(o.created_date, 1, 4) = :year\n" +
        "        and substr(o.created_date, 6, 2) = :month and refund_status = 'APPROVAL'\n" +
        "      group by franchisee_id) ro\n" +
        "         left join franchisee_bank b\n" +
        "                   on ro.franchiseeIndex = b.franchisee_id\n" +
        "         left join (select id, sel_nm as sellerName from franchisee) f\n" +
        "                   on ro.franchiseeIndex = f.id", nativeQuery = true)
    CmsResponseDetailInterface findMonthlyCmsDetail(@Param("franchiseeIndex") Long franchiseeIndex, @Param("year") String year, @Param("month") String month);

    Optional<OrderEntity> findByFranchiseeEntityId(Long franchiseeIndex);


}
