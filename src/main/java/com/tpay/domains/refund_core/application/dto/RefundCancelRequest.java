package com.tpay.domains.refund_core.application.dto;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.domain.RefundEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundCancelRequest {
  private String purchaseSequenceNumber;
  private String takeOutNumber;
  private String name;
  private String nationality;
  private String amount;
  private String passportNumber;

  public static RefundCancelRequest of(CustomerEntity customerEntity, RefundEntity refundEntity) {
    OrderEntity orderEntity = refundEntity.getOrderEntity();

    return RefundCancelRequest.builder()
        .amount(orderEntity.getTotalAmount())
        .name(customerEntity.getCustomerName())
        .nationality(customerEntity.getNation())
        .passportNumber(customerEntity.getPassportNumber())
        .purchaseSequenceNumber(orderEntity.getOrderNumber())
        .takeOutNumber(refundEntity.getTakeOutNumber())
        .build();
  }
}
