package com.tpay.domains.refund.application.dto;

import com.tpay.domains.refund.domain.RefundStatus;

import java.time.LocalDateTime;

public interface RefundFindResponseInterface {
  Long getRefundIndex();
  String getOrderNumber();
  LocalDateTime getCreatedDate();
  String getTotalAmount();
  String getTotalRefund();
  RefundStatus getRefundStatus();
}
