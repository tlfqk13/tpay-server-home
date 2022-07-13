package com.tpay.domains.refund.application.dto;

import com.tpay.domains.refund.domain.RefundStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RefundFindResponseInterface {
    Long getRefundIndex();

    String getOrderNumber();

    LocalDateTime getCreatedDate();

    LocalDate getFormatDate();

    String getTotalAmount();

    String getTotalRefund();

    String getActualAmount();

    RefundStatus getRefundStatus();

    String getBusinessNumber();

    String getStoreName();

    String getFranchiseeId();
}
