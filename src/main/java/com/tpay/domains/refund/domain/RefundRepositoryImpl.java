package com.tpay.domains.refund.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tpay.domains.refund.application.dto.QRefundReceiptDto_Response;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;

import javax.persistence.EntityManager;
import java.util.List;

import static com.tpay.domains.customer.domain.QCustomerEntity.customerEntity;
import static com.tpay.domains.franchisee.domain.QFranchiseeEntity.franchiseeEntity;
import static com.tpay.domains.franchisee_upload.domain.QFranchiseeUploadEntity.franchiseeUploadEntity;
import static com.tpay.domains.order.domain.QOrderEntity.orderEntity;
import static com.tpay.domains.refund.domain.QRefundEntity.refundEntity;

public class RefundRepositoryImpl implements RefundRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RefundRepositoryImpl(EntityManager em){this.queryFactory = new JPAQueryFactory(em);}

    @Override
    public List<RefundReceiptDto.Response> findRefundReceipt(String encryptPassportNumber) {
        List<RefundReceiptDto.Response> content = queryFactory
                .select(new QRefundReceiptDto_Response(
                        franchiseeUploadEntity.taxFreeStoreNumber,
                        orderEntity.saleDate,
                        franchiseeEntity.storeName,
                        franchiseeEntity.sellerName,
                        franchiseeEntity.businessNumber,
                        franchiseeEntity.storeAddressBasic.concat(franchiseeEntity.storeAddressDetail),
                        franchiseeEntity.storeNumber,
                        orderEntity.totalAmount,
                        orderEntity.totalVat,
                        refundEntity.totalRefund
                ))
                .from(orderEntity)
                .leftJoin(orderEntity.refundEntity,refundEntity)
                .leftJoin(orderEntity.franchiseeEntity,franchiseeEntity)
                .leftJoin(orderEntity.customerEntity,customerEntity)
                .leftJoin(franchiseeUploadEntity).on(franchiseeEntity.id.eq(franchiseeUploadEntity.franchiseeIndex))
                .where(customerEntity.passportNumber.eq(encryptPassportNumber))
                .fetch();

        return content;
    }
}
