package com.tpay.domains.order.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tpay.domains.order.application.dto.CmsDetailDto;
import com.tpay.domains.order.application.dto.QCmsDetailDto_Response;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.vat.application.dto.QVatDetailDto_Response;
import com.tpay.domains.vat.application.dto.QVatTotalDto_Response;
import com.tpay.domains.vat.application.dto.VatDetailDto;
import com.tpay.domains.vat.application.dto.VatTotalDto;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.tpay.domains.customer.domain.QCustomerEntity.customerEntity;
import static com.tpay.domains.franchisee_upload.domain.QFranchiseeBankEntity.franchiseeBankEntity;
import static com.tpay.domains.order.domain.QOrderEntity.orderEntity;
import static com.tpay.domains.refund.domain.QRefundEntity.refundEntity;

public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em){this.queryFactory = new JPAQueryFactory(em);}

    @Override
    public List<VatDetailDto.Response> findMonthlyCmsDetailDsl(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate,int pageData) {
        List<VatDetailDto.Response> content = queryFactory
                .select(new QVatDetailDto_Response(
                        orderEntity.orderNumber,
                        orderEntity.createdDate.stringValue().substring(0,10),
                        refundEntity.takeOutNumber,
                        refundEntity.totalRefund,
                        orderEntity.totalAmount,
                        orderEntity.totalVat,
                        customerEntity.customerName,
                        customerEntity.nation
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity,orderEntity)
                .leftJoin(orderEntity.customerEntity,customerEntity)
                .where(orderEntity.franchiseeEntity.id.eq(franchiseeIndex)
                        .and(refundEntity.refundStatus.eq(RefundStatus.APPROVAL))
                        .and(refundEntity.createdDate.between(startLocalDate.atStartOfDay(), LocalDateTime.of(endLocalDate, LocalTime.MAX)))
                )
                .limit(pageData)
                .fetch();

        return content;
    }

    @Override
    public VatTotalDto.Response findMonthlyTotalDsl(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate,boolean isVat) {
        VatTotalDto.Response content = queryFactory
                .select(new QVatTotalDto_Response(
                        orderEntity.orderNumber.count().stringValue(),
                        orderEntity.totalAmount.castToNum(Integer.class).sum().stringValue(),
                        orderEntity.totalVat.castToNum(Integer.class).sum().stringValue(),
                        refundEntity.totalRefund.castToNum(Integer.class).sum().stringValue(),
                        (orderEntity.totalVat.castToNum(Integer.class)
                                .subtract(refundEntity.totalRefund.castToNum(Integer.class))).sum().stringValue()
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity,orderEntity)
                .leftJoin(orderEntity.customerEntity,customerEntity)
                .where(orderEntity.franchiseeEntity.id.eq(franchiseeIndex)
                        .and(refundEntity.refundStatus.eq(RefundStatus.APPROVAL))
                        .and(isVat(isVat,startLocalDate,endLocalDate))
                )
                .fetchOne();

        return content;
    }

    @Override
    public CmsDetailDto.Response findCmsDetail(Long franchiseeIndex) {
        CmsDetailDto.Response content = queryFactory
                .select(new QCmsDetailDto_Response(
                        franchiseeBankEntity.franchiseeEntity.sellerName,
                        franchiseeBankEntity.bankName,
                        franchiseeBankEntity.accountNumber,
                        franchiseeBankEntity.withdrawalDate
                ))
                .from(franchiseeBankEntity)
                .where(franchiseeBankEntity.franchiseeEntity.id.eq(franchiseeIndex))
                .fetchOne();

        return content;
    }

    private BooleanExpression isVat(Boolean isVat, LocalDate startLocalDate, LocalDate endLocalDate) {
        if(isVat){
            // 6개월
            return refundEntity.createdDate.between(startLocalDate.atStartOfDay(), LocalDateTime.of(endLocalDate, LocalTime.MAX));
        }else{
            return refundEntity.createdDate.between(startLocalDate.atStartOfDay(), LocalDateTime.of(endLocalDate, LocalTime.MAX));
        }
    }
}
