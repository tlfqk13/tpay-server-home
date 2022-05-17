package com.tpay.domains.external.application;

import com.tpay.domains.refund.domain.RefundEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentCalculator {

    public String paymentString(RefundEntity refundEntity) {
        int totalAmount = Integer.parseInt(refundEntity.getOrderEntity().getTotalAmount());
        int totalRefund = Integer.parseInt(refundEntity.getTotalRefund());
        return Integer.toString(totalAmount - totalRefund);
    }
}
