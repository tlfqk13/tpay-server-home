package com.tpay.domains.refund.application;

import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
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
                        .takeOutNumber(takeOutNumber)
                        .responseCode(responseCode)
                        .build();

        return refundRepository.save(refundEntity);
    }
}
