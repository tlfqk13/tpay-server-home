package com.tpay.domains.refund.domain;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tpay.domains.customer.application.dto.DepartureStatus;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.order.application.dto.CmsDto;
import com.tpay.domains.order.application.dto.QCmsDto_Response;
import com.tpay.domains.refund.application.dto.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.tpay.domains.barcode.domain.QBarcodeEntity.barcodeEntity;
import static com.tpay.domains.customer.domain.QCustomerEntity.customerEntity;
import static com.tpay.domains.franchisee.domain.QFranchiseeEntity.franchiseeEntity;
import static com.tpay.domains.franchisee_upload.domain.QFranchiseeUploadEntity.franchiseeUploadEntity;
import static com.tpay.domains.order.domain.QOrderEntity.orderEntity;
import static com.tpay.domains.point_scheduled.domain.QPointScheduledEntity.pointScheduledEntity;
import static com.tpay.domains.refund.domain.QRefundAfterEntity.refundAfterEntity;
import static com.tpay.domains.refund.domain.QRefundEntity.refundEntity;

public class RefundRepositoryImpl implements RefundRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RefundRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 2022-12-28 바코드 주소를 넣어야합니다?
    @Override
    public List<RefundReceiptDto.Response> findRefundReceipt(String encryptPassportNumber, boolean refundAfter) {
        List<RefundReceiptDto.Response> content = queryFactory
                .select(new QRefundReceiptDto_Response(
                        orderEntity.barcodeEntity.s3Path,
                        orderEntity.orderNumber,
                        isRefundAfterEntity(),
                        franchiseeUploadEntity.taxFreeStoreNumber,
                        orderEntity.createdDate.stringValue(),
                        franchiseeEntity.storeName,
                        franchiseeEntity.sellerName,
                        franchiseeEntity.businessNumber,
                        franchiseeEntity.storeAddressBasic.concat(franchiseeEntity.storeAddressDetail),
                        franchiseeEntity.storeNumber,
                        orderEntity.totalAmount,
                        orderEntity.totalVat,
                        refundEntity.totalRefund,
                        refundEntity.totalRefund.castToNum(Integer.class).subtract(pointScheduledEntity.value).stringValue(),
                        refundEntity.createdDate
                ))
                .from(orderEntity)
                .leftJoin(orderEntity.refundEntity, refundEntity)
                .leftJoin(orderEntity.barcodeEntity, barcodeEntity)
                .leftJoin(pointScheduledEntity).on(pointScheduledEntity.orderEntity.id.eq(orderEntity.id))
                .leftJoin(orderEntity.franchiseeEntity, franchiseeEntity)
                .leftJoin(orderEntity.customerEntity, customerEntity)
                .leftJoin(franchiseeUploadEntity).on(franchiseeEntity.id.eq(franchiseeUploadEntity.franchiseeIndex))
                .where(customerEntity.passportNumber.eq(encryptPassportNumber)
                        .and(isRefundAfter(refundAfter)))
                .fetch();

        return content;
    }

    @Override
    public List<RefundReceiptDto.Response> downloadsRefundReceipt(String encryptPassportNumber, boolean refundAfter) {
        List<RefundReceiptDto.Response> content = queryFactory
                .select(new QRefundReceiptDto_Response(
                        orderEntity.barcodeEntity.s3Path,
                        orderEntity.orderNumber,
                        isRefundAfterEntity(),
                        franchiseeUploadEntity.taxFreeStoreNumber,
                        orderEntity.createdDate.stringValue(),
                        franchiseeEntity.storeName,
                        franchiseeEntity.sellerName,
                        franchiseeEntity.businessNumber,
                        franchiseeEntity.storeAddressBasic.concat(franchiseeEntity.storeAddressDetail),
                        franchiseeEntity.storeNumber,
                        orderEntity.totalAmount,
                        orderEntity.totalVat,
                        refundEntity.totalRefund,
                        refundEntity.totalRefund.castToNum(Integer.class).subtract(pointScheduledEntity.value).stringValue(),
                        refundEntity.createdDate
                ))
                .from(orderEntity)
                .leftJoin(orderEntity.refundEntity, refundEntity)
                .leftJoin(orderEntity.barcodeEntity, barcodeEntity)
                .leftJoin(pointScheduledEntity).on(pointScheduledEntity.orderEntity.id.eq(orderEntity.id))
                .leftJoin(orderEntity.franchiseeEntity, franchiseeEntity)
                .leftJoin(orderEntity.customerEntity, customerEntity)
                .leftJoin(franchiseeUploadEntity).on(franchiseeEntity.id.eq(franchiseeUploadEntity.franchiseeIndex))
                .where(customerEntity.passportNumber.eq(encryptPassportNumber)
                        .and(isRefundAfter(refundAfter)))
                .fetch();

        return content;
    }


    @Override
    public List<RefundFindDto.Response> findRefundDetail(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate) {
        List<RefundFindDto.Response> content = queryFactory
                .select(new QRefundFindDto_Response(
                        refundEntity.id,
                        refundEntity.refundStatus,
                        refundEntity.createdDate,
                        orderEntity.totalAmount,
                        refundEntity.totalRefund,
                        (orderEntity.totalAmount.castToNum(Integer.class)
                                .subtract(refundEntity.totalRefund.castToNum(Integer.class)))
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity, orderEntity)
                .leftJoin(orderEntity.franchiseeEntity, franchiseeEntity)
                .where(franchiseeEntity.id.eq(franchiseeIndex)
                        .and(refundEntity.createdDate
                                .between(startLocalDate.atStartOfDay(), LocalDateTime.of(endLocalDate, LocalTime.MAX))))
                .orderBy(refundEntity.createdDate.desc())
                .fetch();

        return content;
    }

    @Override
    public Page<RefundFindAllDto.Response> findRefundAll(
            Pageable pageable, LocalDate startLocalDate, LocalDate endLocalDate
            , boolean isKeywordEmpty, boolean businessNumber, String searchKeyword
            , RefundStatus refundStatus, RefundType refundType
            , DepartureStatus departureStatus, PaymentStatus paymentStatus) {

        List<RefundFindAllDto.Response> content = queryFactory
                .select(new QRefundFindAllDto_Response(
                        refundEntity.id,
                        customerEntity.customerName,
                        customerEntity.nation,
                        refundEntity.createdDate,
                        orderEntity.totalAmount,
                        refundEntity.totalRefund,
                        (orderEntity.totalAmount.castToNum(Integer.class)
                                .subtract(refundEntity.totalRefund.castToNum(Integer.class))),
                        refundEntity.refundStatus,
                        franchiseeEntity.businessNumber,
                        franchiseeEntity.storeName,
                        refundEntity.refundAfterEntity.paymentStatus,
                        customerEntity.departureStatus
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity, orderEntity)
                .leftJoin(refundEntity.refundAfterEntity, refundAfterEntity)
                .leftJoin(orderEntity.franchiseeEntity, franchiseeEntity)
                .leftJoin(orderEntity.customerEntity, customerEntity)
                .where(refundEntity.createdDate.between(startLocalDate.atStartOfDay(), LocalDateTime.of(endLocalDate, LocalTime.MAX))
                        .and(franchiseeEntity.storeName.ne("석세스모드"))
                        .and(isKeywordEmpty(isKeywordEmpty, businessNumber, searchKeyword, refundStatus))
                        .and(refundFilter(refundType, refundStatus, departureStatus, paymentStatus)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(refundEntity.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(refundEntity.count())
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity, orderEntity)
                .leftJoin(orderEntity.franchiseeEntity, franchiseeEntity)
                .leftJoin(orderEntity.customerEntity, customerEntity)
                .where(refundEntity.createdDate.between(startLocalDate.atStartOfDay(), LocalDateTime.of(endLocalDate, LocalTime.MAX))
                        .and(franchiseeEntity.storeName.ne("석세스모드"))
                        .and(isKeywordEmpty(isKeywordEmpty, businessNumber, searchKeyword, refundStatus))
                        .and(refundFilter(refundType, refundStatus, departureStatus, paymentStatus)));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private Predicate refundFilter(RefundType refundType, RefundStatus refundStatus, DepartureStatus departureStatus, PaymentStatus paymentStatus) {
        if (refundType.equals(RefundType.IMMEDIATE)) { // 즉시
            if (refundStatus.equals(RefundStatus.APPROVAL)) {
                return isImmediate()
                        .and(isRefundStatus(refundStatus));
            } else if (refundStatus.equals(RefundStatus.CANCEL)) {
                return isImmediate()
                        .and(isRefundStatus(refundStatus));
            } else {
                return isImmediate();
            }
        } else if (refundType.equals(RefundType.AFTER)) { // 사후
            return departurePaymentStatus(paymentStatus, departureStatus)
                    .and(isRefundAfterEntity())
                    .and(isAfter());
        } else { // 전체
            if (refundStatus.equals(RefundStatus.APPROVAL)) {
                return isRefundStatus(refundStatus);
            } else if (refundStatus.equals(RefundStatus.CANCEL)) {
                return isRefundStatus(refundStatus);
            } else {
                return null;
            }
        }
    }

    private BooleanExpression isRefundStatus(RefundStatus refundStatus) {
        return refundEntity.refundStatus.in(refundStatus);
    }

    private BooleanExpression departurePaymentStatus(PaymentStatus paymentStatus, DepartureStatus departureStatus) {
        if (departureStatus.equals(DepartureStatus.DEPARTURE_WAIT)) {
            if (paymentStatus.equals(PaymentStatus.PAYMENT_WAIT)) {
                return isDeparturePaymentStatus(paymentStatus, departureStatus)
                        .and(isRefundAfterEntity());
            } else {
                return isRefundAfterEntity();
            }
        } else if (departureStatus.equals(DepartureStatus.DEPARTURE_COMPLETE)) {
            if (paymentStatus.equals(PaymentStatus.PAYMENT_WAIT)) {
                return isDeparturePaymentStatus(paymentStatus, departureStatus)
                        .and(isRefundAfterEntity());
            } else if (paymentStatus.equals(PaymentStatus.PAYMENT_COMPLETE)) {
                return isDeparturePaymentStatus(paymentStatus, departureStatus)
                        .and(isRefundAfterEntity());
            }
        } else { // Departure ALL
            if (paymentStatus.equals(PaymentStatus.PAYMENT_WAIT)) {
                return isDeparturePaymentStatus(paymentStatus, departureStatus)
                        .and(isRefundAfterEntity());
            } else if (paymentStatus.equals(PaymentStatus.PAYMENT_COMPLETE)) {
                return isDeparturePaymentStatus(paymentStatus, departureStatus)
                        .and(isRefundAfterEntity());
            } else {
                return isRefundAfterEntity();
            }
        }
        return isRefundAfterEntity();
    }

    private BooleanExpression isDeparturePaymentStatus(PaymentStatus paymentStatus, DepartureStatus departureStatus) {
        if (DepartureStatus.ALL.equals(departureStatus)) {
            return refundEntity.refundAfterEntity.paymentStatus.in(paymentStatus);
        } else {
            return customerEntity.departureStatus.in(departureStatus)
                    .and(refundEntity.refundAfterEntity.paymentStatus.in(paymentStatus));
        }
    }

    private BooleanExpression isRefundAfterEntity() {
        return refundEntity.refundAfterEntity.isNotNull();
    }

    @NotNull
    private BooleanExpression isAfter() {
        return refundEntity.takeOutNumber.contains("A");
    }

    @NotNull
    private BooleanExpression isImmediate() {
        return refundEntity.totalRefund.castToNum(Integer.class).loe(74000)
                .and(refundEntity.takeOutNumber.contains("B"));
    }

    @Override
    public List<CmsDto.Response> findFranchiseeIdCmsService(LocalDate startDate, LocalDate endDate) {
        List<CmsDto.Response> content = queryFactory
                .select(new QCmsDto_Response(
                        franchiseeEntity.id
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity, orderEntity)
                .leftJoin(orderEntity.franchiseeEntity, franchiseeEntity)
                .where(orderEntity.createdDate.between(startDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX))
                        .and(refundEntity.refundStatus.eq(RefundStatus.APPROVAL)
                                .and(franchiseeEntity.id.ne(152L))))
                .groupBy(franchiseeEntity.id)
                .fetch();

        return content;
    }

    @Override
    public List<CmsDto.Response> findFranchiseeIdAfter(LocalDate startDate, LocalDate endDate) {
        List<CmsDto.Response> content = queryFactory
                .select(new QCmsDto_Response(
                        franchiseeEntity.id
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity, orderEntity)
                .leftJoin(orderEntity.franchiseeEntity, franchiseeEntity)
                .leftJoin(refundEntity.refundAfterEntity, refundAfterEntity)
                .where(orderEntity.createdDate.between(startDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX))
                        .and(refundEntity.refundStatus.eq(RefundStatus.APPROVAL)
                                .and(franchiseeEntity.id.ne(152L)))
                        .and(refundEntity.refundAfterEntity.isNotNull()))
                .groupBy(franchiseeEntity.id)
                .fetch();

        return content;
    }


    @Override
    public RefundDetailDto.Response findRefundDetail(Long refundIndex) {
        RefundDetailDto.Response content = queryFactory
                .select(new QRefundDetailDto_Response(
                        refundEntity.createdDate,
                        franchiseeEntity.storeName,
                        customerEntity.customerName,
                        customerEntity.nation,
                        orderEntity.totalAmount,
                        refundEntity.totalRefund,
                        customerEntity.departureStatus,
                        refundEntity.refundAfterEntity.paymentStatus,
                        customerEntity.customerEmail
                ))
                .from(orderEntity)
                .leftJoin(orderEntity.refundEntity, refundEntity)
                .leftJoin(orderEntity.customerEntity, customerEntity)
                .leftJoin(orderEntity.franchiseeEntity, franchiseeEntity)
                .leftJoin(refundEntity.refundAfterEntity, refundAfterEntity)
                .where(refundEntity.id.eq(refundIndex))
                .fetchOne();

        return content;
    }

    @Override
    public RefundPaymentDetailDto.Response findRefundPaymentDetail(Long refundIndex) {
        RefundPaymentDetailDto.Response content = queryFactory
                .select(new QRefundPaymentDetailDto_Response(
                        customerEntity.customerPaymentType,
                        customerEntity.customerBankName,
                        customerEntity.customerAccountNumber,
                        customerEntity.customerCreditNumber
                ))
                .from(orderEntity)
                .leftJoin(orderEntity.customerEntity, customerEntity)
                .leftJoin(orderEntity.refundEntity, refundEntity)
                .where(refundEntity.id.eq(refundIndex))
                .fetchOne();

        return content;
    }

    private BooleanExpression isKeywordEmpty(Boolean isKeywordEmpty, Boolean businessNumber, String keyword, RefundStatus refundStatus) {
        if (isKeywordEmpty) {
            if (refundStatus.equals(RefundStatus.ALL)) {
                return null;
            } else {
                return isRefundStatus(refundStatus);
            }
        } else {
            if (businessNumber) {
                if (refundStatus.equals(RefundStatus.ALL)) {
                    return franchiseeEntity.businessNumber.eq(keyword);
                } else {
                    return franchiseeEntity.businessNumber.eq(keyword)
                            .and(isRefundStatus(refundStatus));
                }
            } else {
                if (refundStatus.equals(RefundStatus.ALL)) {
                    return franchiseeEntity.storeName.eq(keyword);
                } else {
                    return franchiseeEntity.storeName.eq(keyword).and(isRefundStatus(refundStatus));
                }
            }
        }
    }

    private BooleanExpression isRefundAfter(Boolean refundAfter) {
        if (refundAfter) {
            return isRefundAfterEntity().and(isAfter());
        } else {
            return isImmediate().and(refundEntity.refundAfterEntity.isNull());
        }
    }
}
