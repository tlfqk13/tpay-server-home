package com.tpay.domains.order.domain;

import com.tpay.domains.order.application.dto.OrdersDtoInterface;
import com.tpay.domains.vat.application.dto.VatDetailResponseInterface;
import com.tpay.domains.vat.application.dto.VatReportResponseInterface;
import com.tpay.domains.vat.application.dto.VatTotalResponseInterface;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, OrderRepositoryCustom{

    void deleteByFranchiseeEntityId(Long franchiseeIndex);
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
        "      ,IFNULL(sum( cast (r.tot_refund as INTEGER)),0) as totalRefund\n" +
        "      from orders o inner join refund r on o.id = r.order_id\n" +
        "      where franchisee_id = :franchiseeIndex\n" +
        "      and refund_status = 'APPROVAL' and o.created_date between :startDate and :endDate\n" +
            "    and r.refund_after_id is null\n" , nativeQuery = true)
    VatTotalResponseInterface findQuarterlyTotal(@Param("franchiseeIndex") Long franchiseeIndex, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query(value = "select\n" +
            "    purchs_sn                                as purchaseSerialNumber\n" +
            "    ,substr(replace(o.created_date,'-',''),1,8) as saleDate\n" +
            "    ,tk_out_conf_no                             as takeoutConfirmNumber\n" +
            "    ,r.tot_refund                                 as refundAmount\n" +
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
    Optional<OrderEntity> findByFranchiseeEntityId(Long franchiseeIndex);

    @Query(value =  "select\n " +
            "  o.purchs_sn as docId\n" +
            " ,f.store_nm as shopNm\n" +
            " ,f.prd_nm as shopTypeCcd\n" +
            " ,o.sale_datm as purchsDate\n" +
            " ,o.tot_amt as totPurchsAmt\n" +
            " ,o.tot_vat as vat\n" +
            " ,r.tot_refund as totalRefund\n" +
            " ,r.refund_status as customsCleanceYn" +
            " ,rf.refund_after_method as earlyRfndYn" +
            " from orders o inner join franchisee f on o.franchisee_id = f.id\n" +
            "               left join customer c on c.id = o.customer_id\n" +
            "               left join refund r on o.id = r.order_id" +
            "               left join refund_after rf on r.refund_after_id = rf.refund_after_id\n" +
            " where c.cus_pass_no = :passportNumber\n" +
            " and r.refund_after_id is not null and r.tk_out_conf_no = ''\n" +
            " and rf.payment_id is null\n" +
            " and r.refund_status in (1, 4)\n" +
            " order by r.id desc ",nativeQuery = true
    )
    List<OrdersDtoInterface> findVanOrdersDetail(@Param("passportNumber") String passportNumber);

    @EntityGraph(attributePaths = {"customerEntity"})
    Optional<OrderEntity> findByOrderNumber(String docId);

    @Query(value = "select o from OrderEntity o join fetch o.customerEntity join fetch o.refundEntity where o.orderNumber in :docIds")
    List<OrderEntity> findByOrderNumbers(List<String> docIds);

    @Query(value = "select o from OrderEntity o  join fetch o.customerEntity " +
            "where o.customerEntity.id = :customerId and o.orderNumber is not null and o.customerEntity.id is not null")
    List<OrderEntity> findOrders(@Param("customerId")Long customerId);

    @Query(value = "select o from OrderEntity o " +
            "where o.orderNumber = :barcode")
    List<OrderEntity> findOrdersPassportMapping(String barcode);

    @Query(value =  "select\n " +
            "  o.purchs_sn as docId\n" +
            " ,f.store_nm as shopNm\n" +
            " ,f.prd_nm as shopTypeCcd\n" +
            " ,o.sale_datm as purchsDate\n" +
            " ,o.tot_amt as totPurchsAmt\n" +
            " ,o.tot_vat as vat\n" +
            " ,r.tot_refund as totalRefund\n" +
            " ,r.refund_status as customsCleanceYn" +
            " ,rf.refund_after_method as earlyRfndYn" +
            " from orders o inner join franchisee f on o.franchisee_id = f.id\n" +
            "               left join customer c on c.id = o.customer_id\n" +
            "               left join refund r on o.id = r.order_id" +
            "               left join refund_after rf on r.refund_after_id = rf.refund_after_id\n" +
            " where c.cus_pass_no = :passportNumber\n" +
            " and r.refund_after_id is not null and r.tk_out_conf_no = ''\n" +
            " and r.refund_status in (1, 4) \n" +
            " and rf.payment_id is null and o.purchs_sn = :barcode\n" +
            " order by r.id desc ",nativeQuery = true
    )
    List<OrdersDtoInterface> findVanOrdersDetail(String passportNumber, String barcode);
}
