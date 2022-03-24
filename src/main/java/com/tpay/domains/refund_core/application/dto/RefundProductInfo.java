package com.tpay.domains.refund_core.application.dto;

import com.tpay.domains.order.domain.OrderLineEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundProductInfo {
    private String productName;
    private String productPrice;
    private String productQuantity;
    private String productSequenceNumber;
    private String productCode;
    private String salePrice;
    private String indVAT;
    private String indIct;
    private String indEdut;
    private String indStr;

    public static RefundProductInfo of(OrderLineEntity orderLineEntity) {
        return RefundProductInfo.builder()
            .productSequenceNumber(orderLineEntity.getProductEntity().getLineNumber())
            .productQuantity(orderLineEntity.getQuantity())
            .productPrice(orderLineEntity.getProductEntity().getPrice())
            .productName(orderLineEntity.getProductEntity().getName())
            .productCode(orderLineEntity.getProductEntity().getCode())
            .salePrice(orderLineEntity.getTotalPrice())
            .indStr("0")
            .indVAT(orderLineEntity.getVat())
            .indIct("0")
            .indEdut("0")
            .build();
    }
}
