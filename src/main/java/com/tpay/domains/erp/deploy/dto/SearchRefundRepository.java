package com.tpay.domains.erp.deploy.dto;

import com.tpay.domains.refund.application.dto.RefundFindResponseInterface;
import com.tpay.domains.refund.domain.RefundEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SearchRefundRepository extends JpaRepository<RefundEntity, Long> {

    @Query(value =
            "select r.id            as refundIndex,\n" +
                    "       c.cus_nm        as customerName,\n" +
                    "       c.cus_natn      as customerNational,\n" +
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
                    "         left join customer c on c.id = o.customer_id\n" +
                    "where r.created_date between :startLocalDate and :endLocalDate\n" +
                    "           and f.biz_no like %:searchKeyword%\n" +
                    "and f.store_nm != '석세스모드'\n" +
                    "order by refundIndex desc\n",
            countQuery = "select r.id            as refundIndex,\n" +
                    "       c.cus_nm        as customerName,\n" +
                    "       c.cus_natn      as customerNational,\n" +
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
                    "         left join customer c on c.id = o.customer_id\n" +
                    "where r.created_date between :startLocalDate and :endLocalDate\n" +
                    "           and f.biz_no like %:searchKeyword%\n" +
                    "and f.store_nm != '석세스모드'\n" +
                    "order by refundIndex desc", nativeQuery = true)
    Page<RefundFindResponseInterface> SearchFindByBusinessNumber(Pageable pageable, @Param("startLocalDate") LocalDate startLocalDate, @Param("endLocalDate") LocalDate endLocalDate, @Param("searchKeyword") String searchKeyword);

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
                    "         left join customer c on c.id = o.customer_id\n" +
                    "where r.created_date between :startLocalDate and :endLocalDate\n" +
                    "           and f.biz_no like %:searchKeyword%\n" +
                    "and refund_status = :ordinal\n" +
                    "and f.store_nm != '석세스모드'\n" +
                    "order by refundIndex desc\n",
            countQuery = "select r.id            as refundIndex,\n" +
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
                    "         left join customer c on c.id = o.customer_id\n" +
                    "where r.created_date between :startLocalDate and :endLocalDate\n" +
                    "           and f.biz_no like %:searchKeyword%\n" +
                    "and refund_status = :ordinal\n" +
                    "and f.store_nm != '석세스모드'\n" +
                    "order by refundIndex desc", nativeQuery = true)
    Page<RefundFindResponseInterface> SearchFindByBusinessNumber(Pageable pageable, @Param("startLocalDate") LocalDate startLocalDate, @Param("endLocalDate") LocalDate endLocalDate
            , @Param("searchKeyword") String searchKeyword, @Param("ordinal") Integer ordinal);


    @Query(value =
            "select r.id            as refundIndex,\n" +
                    "       c.cus_nm        as customerName,\n" +
                    "       c.cus_natn      as customerNational,\n" +
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
                    "         left join customer c on c.id = o.customer_id\n" +
                    "where r.created_date between :startLocalDate and :endLocalDate\n" +
                    "           and f.store_nm like %:searchKeyword%\n" +
                    "and f.biz_no != 2141582141\n" +
                    "order by refundIndex desc\n",
            countQuery = "select r.id            as refundIndex,\n" +
                    "       c.cus_nm        as customerName,\n" +
                    "       c.cus_natn      as customerNational,\n" +
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
                    "         left join customer c on c.id = o.customer_id\n" +
                    "where r.created_date between :startLocalDate and :endLocalDate\n" +
                    "           and f.store_nm like %:searchKeyword%\n" +
                    "and f.biz_no != 2141582141\n" +
                    "order by refundIndex desc", nativeQuery = true)
    Page<RefundFindResponseInterface> SearchFindByStoreName(Pageable pageable, @Param("startLocalDate") LocalDate startLocalDate, @Param("endLocalDate") LocalDate endLocalDate, @Param("searchKeyword") String searchKeyword);

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
                    "           and f.store_nm like %:searchKeyword%\n" +
                    "and refund_status = :ordinal\n" +
                    "and f.biz_no != 2141582141\n" +
                    "order by refundIndex desc\n",
            countQuery = "select r.id            as refundIndex,\n" +
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
                    "           and f.store_nm like %:searchKeyword%\n" +
                    "and refund_status = :ordinal\n" +
                    "and f.biz_no != 2141582141\n" +
                    "order by refundIndex desc", nativeQuery = true)
    Page<RefundFindResponseInterface> SearchFindByStoreName(Pageable pageable, @Param("startLocalDate") LocalDate startLocalDate, @Param("endLocalDate") LocalDate endLocalDate
            , @Param("searchKeyword") String searchKeyword, @Param("ordinal") Integer ordinal);

}
