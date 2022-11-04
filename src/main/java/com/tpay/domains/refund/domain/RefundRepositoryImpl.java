package com.tpay.domains.refund.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tpay.domains.order.application.dto.CmsDto;
import com.tpay.domains.order.application.dto.QCmsDto_Response;
import com.tpay.domains.refund.application.dto.QRefundReceiptDto_Response;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import com.tpay.domains.refund_test.dto.QRefundFindAllDto_Response;
import com.tpay.domains.refund_test.dto.QRefundFindDto_Response;
import com.tpay.domains.refund_test.dto.RefundFindAllDto;
import com.tpay.domains.refund_test.dto.RefundFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.tpay.domains.customer.domain.QCustomerEntity.customerEntity;
import static com.tpay.domains.franchisee.domain.QFranchiseeEntity.franchiseeEntity;
import static com.tpay.domains.franchisee_upload.domain.QFranchiseeUploadEntity.franchiseeUploadEntity;
import static com.tpay.domains.order.domain.QOrderEntity.orderEntity;
import static com.tpay.domains.point_scheduled.domain.QPointScheduledEntity.pointScheduledEntity;
import static com.tpay.domains.refund.domain.QRefundEntity.refundEntity;
public class RefundRepositoryImpl implements RefundRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RefundRepositoryImpl(EntityManager em){this.queryFactory = new JPAQueryFactory(em);}

    @Override
    public List<RefundReceiptDto.Response> findRefundReceipt(String encryptPassportNumber, boolean refundAfter) {
        List<RefundReceiptDto.Response> content = queryFactory
                .select(new QRefundReceiptDto_Response(
                        refundEntity.refundAfterEntity.isNotNull(),
                        franchiseeUploadEntity.taxFreeStoreNumber,
                        orderEntity.createdDate,
                        franchiseeEntity.storeName,
                        franchiseeEntity.sellerName,
                        franchiseeEntity.businessNumber,
                        franchiseeEntity.storeAddressBasic.concat(franchiseeEntity.storeAddressDetail),
                        franchiseeEntity.storeNumber,
                        orderEntity.totalAmount,
                        orderEntity.totalVat,
                        refundEntity.totalRefund,
                        refundEntity.totalRefund.castToNum(Integer.class).subtract(pointScheduledEntity.value).stringValue(),
                        refundEntity.createdDate.stringValue().substring(0,10)
                ))
                .from(orderEntity)
                .leftJoin(orderEntity.refundEntity,refundEntity)
                .leftJoin(pointScheduledEntity).on(pointScheduledEntity.orderEntity.id.eq(orderEntity.id))
                .leftJoin(orderEntity.franchiseeEntity,franchiseeEntity)
                .leftJoin(orderEntity.customerEntity,customerEntity)
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
               .innerJoin(refundEntity.orderEntity,orderEntity)
               .leftJoin(orderEntity.franchiseeEntity,franchiseeEntity)
               .where(franchiseeEntity.id.eq(franchiseeIndex)
                       .and(refundEntity.createdDate
                               .between(startLocalDate.atStartOfDay(),LocalDateTime.of(endLocalDate, LocalTime.MAX))))
               .fetch();

        return content;
    }

    @Override
    public Page<RefundFindAllDto.Response> findRefundAll(Pageable pageable, LocalDate startLocalDate, LocalDate endLocalDate
            , boolean isKeywordEmpty, boolean businessNumber, String searchKeyword,RefundStatus refundStatus) {

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
                        franchiseeEntity.storeName
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity,orderEntity)
                .leftJoin(orderEntity.franchiseeEntity,franchiseeEntity)
                .leftJoin(orderEntity.customerEntity,customerEntity)
                .where(refundEntity.createdDate.between(startLocalDate.atStartOfDay(),LocalDateTime.of(endLocalDate, LocalTime.MAX))
                        .and(franchiseeEntity.storeName.ne("석세스모드"))
                        .and(isKeywordEmpty(isKeywordEmpty,businessNumber,searchKeyword,refundStatus)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(refundEntity.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(refundEntity.count())
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity,orderEntity)
                .leftJoin(orderEntity.franchiseeEntity,franchiseeEntity)
                .leftJoin(orderEntity.customerEntity,customerEntity);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<CmsDto.Response> findFranchiseeIdCmsService(LocalDate startDate,LocalDate endDate) {
        List<CmsDto.Response> content = queryFactory
                .select(new QCmsDto_Response(
                        franchiseeEntity.id
                ))
                .from(refundEntity)
                .innerJoin(refundEntity.orderEntity,orderEntity)
                .leftJoin(orderEntity.franchiseeEntity,franchiseeEntity)
                .where(orderEntity.createdDate.between(startDate.atStartOfDay(),LocalDateTime.of(endDate, LocalTime.MAX))
                        .and(refundEntity.refundStatus.eq(RefundStatus.APPROVAL)
                        .and(franchiseeEntity.id.ne(152L))))
                .groupBy(franchiseeEntity.id)
                .fetch();

        return content;
    }

    private BooleanExpression isKeywordEmpty(Boolean isKeywordEmpty, Boolean businessNumber, String keyword,RefundStatus refundStatus) {
        if(isKeywordEmpty){
            if(refundStatus.equals(RefundStatus.ALL)){
                return null;
            }else {
                return refundEntity.refundStatus.in(refundStatus);
            }
        }else{
            if(businessNumber){
                if(refundStatus.equals(RefundStatus.ALL)){
                    return franchiseeEntity.businessNumber.eq(keyword);
                }
                return franchiseeEntity.businessNumber.eq(keyword).and(refundEntity.refundStatus.in(refundStatus));
            }else{
                if(refundStatus.equals(RefundStatus.ALL)){
                    return franchiseeEntity.businessNumber.eq(keyword);
                }
                return franchiseeEntity.storeName.eq(keyword).and(refundEntity.refundStatus.in(refundStatus));
            }
        }
    }

    private BooleanExpression isRefundAfter(Boolean refundAfter) {
        if(refundAfter){
            return refundEntity.refundAfterEntity.isNotNull()
                    .and(refundEntity.totalRefund.castToNum(Integer.class).goe(80000));
        }else{
            return refundEntity.totalRefund.castToNum(Integer.class).loe(74000);
        }
    }

}
