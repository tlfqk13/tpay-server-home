package com.tpay.domains.order.domain;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.order.application.dto.CmsDetailDto;
import com.tpay.domains.order.application.dto.QCmsDetailDto_Response;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.vat.application.dto.*;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.tpay.domains.customer.domain.QCustomerEntity.customerEntity;
import static com.tpay.domains.franchisee.domain.QFranchiseeEntity.franchiseeEntity;
import static com.tpay.domains.franchisee_upload.domain.QFranchiseeBankEntity.franchiseeBankEntity;
import static com.tpay.domains.order.domain.QOrderEntity.orderEntity;
import static com.tpay.domains.refund.domain.QRefundAfterEntity.refundAfterEntity;
import static com.tpay.domains.refund.domain.QRefundEntity.refundEntity;

public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<VatDetailDto.Response> findMonthlyCmsVatDetail(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate, int pageData, RefundType refundType) {
        List<VatDetailDto.Response> content = queryFactory
                .select(new QVatDetailDto_Response(
                        orderEntity.orderNumber,
                        orderEntity.createdDate.stringValue().substring(0, 10),
                        refundEntity.takeOutNumber,
                        refundEntity.totalRefund,
                        orderEntity.totalAmount,
                        orderEntity.totalVat,
                        customerEntity.customerName,
                        customerEntity.nation,
                        refundEntity.refundAfterEntity.approvalFinishDate.substring(0,10),
                        refundEntity.refundAfterEntity.modifiedDate.stringValue().substring(0,10)
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity, orderEntity)
                .leftJoin(orderEntity.customerEntity, customerEntity)
                .leftJoin(refundEntity.refundAfterEntity,refundAfterEntity)
                .where(orderEntity.franchiseeEntity.id.eq(franchiseeIndex)
                        .and(refundEntity.refundStatus.eq(RefundStatus.APPROVAL))
                        .and(refundEntity.createdDate.between(startLocalDate.atStartOfDay(), LocalDateTime.of(endLocalDate, LocalTime.MAX)))
                        .and(refundFilter(refundType))
                )
                .limit(pageData)
                .fetch();

        return content;
    }

    @Override
    public VatTotalDto.Response findMonthlyTotal(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate, RefundType refundType) {
        VatTotalDto.Response content = queryFactory
                .select(new QVatTotalDto_Response(
                        orderEntity.orderNumber.count().stringValue(),
                        orderEntity.totalAmount.castToNum(Integer.class).sum().stringValue(), //판매금액
                        orderEntity.totalVat.castToNum(Integer.class).sum().stringValue(), // 부가가치세
                        refundEntity.totalRefund.castToNum(Integer.class).sum().stringValue(), // 즉시환급상당액
                        (orderEntity.totalVat.castToNum(Integer.class)
                                .subtract(refundEntity.totalRefund.castToNum(Integer.class))).sum().stringValue(), // 환급 수수료
                        (orderEntity.totalAmount.castToNum(Integer.class)
                                .subtract(orderEntity.totalVat.castToNum(Integer.class))).sum().stringValue() // 공급가 = 판매금액 - 부가가치세
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity,orderEntity)
                .leftJoin(orderEntity.customerEntity,customerEntity)
                .leftJoin(refundEntity.refundAfterEntity,refundAfterEntity)
                .where(orderEntity.franchiseeEntity.id.eq(franchiseeIndex)
                        .and(refundEntity.refundStatus.eq(RefundStatus.APPROVAL))
                        .and(refundEntity.createdDate.between(startLocalDate.atStartOfDay(), LocalDateTime.of(endLocalDate, LocalTime.MAX)))
                        .and(refundFilter(refundType))
                )
                .fetchOne();

        return content;
    }

    private Predicate refundFilter(RefundType refundType){
        if(RefundType.IMMEDIATE.equals(refundType)){
            return orderEntity.totalAmount.castToNum(Integer.class).lt(500000)
                    .and(refundEntity.refundAfterEntity.isNull());
        }else if (RefundType.AFTER.equals(refundType)){
            return orderEntity.totalAmount.castToNum(Integer.class).goe(500000)
                    .or(refundEntity.refundAfterEntity.isNotNull());
        }else {
            System.out.println("refund Fileter ALL");
            return null;
        }

    }

    @Override
    public CmsDetailDto.Response findCmsBankInfo(Long franchiseeIndex) {
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

    @Override
    public List<OrderEntity> findRefundAfterOrdersBetweenDates(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        return queryFactory.selectFrom(orderEntity)
                .leftJoin(orderEntity.refundEntity, refundEntity).fetchJoin()
                .leftJoin(refundEntity.refundAfterEntity, refundAfterEntity).fetchJoin()
                .leftJoin(orderEntity.franchiseeEntity, franchiseeEntity).fetchJoin()
                .where(orderEntity.franchiseeEntity.id.eq(franchiseeIndex),
                        orderEntity.refundEntity.refundStatus.eq(RefundStatus.APPROVAL),
                        orderEntity.createdDate.between(startDate.atStartOfDay(), endDate.atStartOfDay()),
                        orderEntity.refundEntity.refundAfterEntity.isNotNull(),
                        refundAfterEntity.approvalFinishDate.isNotNull()
                        )
                .fetch();
    }

    @Override
    public HometaxTailDto findRefundAfterOrdersTotalBetweenDates(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(
                        new QHometaxTailDto(
                                orderEntity.count().stringValue(),
                                orderEntity.totalAmount.castToNum(Integer.class).sum().stringValue(),
                                orderEntity.totalRefund.castToNum(Integer.class).sum().stringValue(),
                                orderEntity.totalVat.castToNum(Integer.class).sum().stringValue()
                        ))
                .from(orderEntity)
                .leftJoin(orderEntity.refundEntity, refundEntity)
                .leftJoin(refundEntity.refundAfterEntity, refundAfterEntity)
                .leftJoin(orderEntity.franchiseeEntity, franchiseeEntity)
                .where(orderEntity.franchiseeEntity.id.eq(franchiseeIndex),
                        refundEntity.refundStatus.eq(RefundStatus.APPROVAL),
                        refundAfterEntity.approvalFinishDate.isNotNull(),
                        orderEntity.refundEntity.refundAfterEntity.isNotNull(),
                        orderEntity.createdDate.between(startDate.atStartOfDay(), endDate.atStartOfDay())
                )
                .fetchOne();
    }

    @Override
    public VatTotalDto.Response findMonthlyTotal(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate) {
        VatTotalDto.Response content = queryFactory
                .select(new QVatTotalDto_Response(
                        orderEntity.orderNumber.count().stringValue(),
                        orderEntity.totalAmount.castToNum(Integer.class).sum().stringValue(), //판매금액
                        orderEntity.totalVat.castToNum(Integer.class).sum().stringValue(), // 부가가치세
                        refundEntity.totalRefund.castToNum(Integer.class).sum().stringValue(), // 즉시환급상당액
                        (orderEntity.totalVat.castToNum(Integer.class)
                                .subtract(refundEntity.totalRefund.castToNum(Integer.class))).sum().stringValue(), // 환급 수수료
                        (orderEntity.totalAmount.castToNum(Integer.class)
                                .subtract(orderEntity.totalVat.castToNum(Integer.class))).sum().stringValue() // 공급가 = 판매금액 - 부가가치세
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity,orderEntity)
                .leftJoin(orderEntity.customerEntity,customerEntity)
                .where(orderEntity.franchiseeEntity.id.eq(franchiseeIndex)
                        .and(refundEntity.refundStatus.eq(RefundStatus.APPROVAL))
                        .and(refundEntity.createdDate.between(startLocalDate.atStartOfDay(), LocalDateTime.of(endLocalDate, LocalTime.MAX)))
                )
                .fetchOne();

        return content;
    }

    @Override
    public List<VatDetailDto.Response> findMonthlyCmsVatDetail(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate, int pageData) {
        List<VatDetailDto.Response> content = queryFactory
                .select(new QVatDetailDto_Response(
                        orderEntity.orderNumber,
                        orderEntity.createdDate.stringValue().substring(0, 10),
                        refundEntity.takeOutNumber,
                        refundEntity.totalRefund,
                        orderEntity.totalAmount,
                        orderEntity.totalVat,
                        customerEntity.customerName,
                        customerEntity.nation,
                        orderEntity.createdDate.stringValue().substring(0, 10),
                        orderEntity.createdDate.stringValue().substring(0, 10)
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity, orderEntity)
                .leftJoin(orderEntity.customerEntity, customerEntity)
                .where(orderEntity.franchiseeEntity.id.eq(franchiseeIndex)
                        .and(refundEntity.refundStatus.eq(RefundStatus.APPROVAL))
                        .and(refundEntity.createdDate.between(startLocalDate.atStartOfDay(), LocalDateTime.of(endLocalDate, LocalTime.MAX)))
                )
                .limit(pageData)
                .fetch();

        return content;
    }
}
