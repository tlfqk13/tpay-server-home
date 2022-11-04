package com.tpay.domains.refund.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.OrderNotFoundException;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefundService {

    private final RefundRepository refundRepository;

    @Transactional
    public RefundEntity save(String responseCode, String purchaseSn, String takeOutNumber, OrderEntity orderEntity) {
        RefundEntity refundEntity =
            RefundEntity.builder()
                .responseCode(responseCode)
                .orderNumber(purchaseSn)
                .takeOutNumber(takeOutNumber)
                .orderEntity(orderEntity)
                .build();

        return refundRepository.save(refundEntity);
    }

    @Transactional
    public RefundEntity save(String responseCode, String takeOutNumber, OrderEntity orderEntity) {
        RefundEntity refundEntity =
                RefundEntity.builder()
                        .orderEntity(orderEntity)
                        .orderNumber(orderEntity.getOrderNumber())
                        .takeOutNumber(takeOutNumber)
                        .responseCode(responseCode)
                        .build();

        return refundRepository.save(refundEntity);
    }

    public RefundEntity getRefundByTkOutNumber(String tkOutNum) {
        return refundRepository.findByTakeOutNumber(tkOutNum)
                .orElseThrow(() -> new OrderNotFoundException(ExceptionState.ORDER_NOT_FOUND, "TkOutNumber 로 주문을 찾을 수 없음"));
    }
}
