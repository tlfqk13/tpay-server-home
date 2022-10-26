package com.tpay.domains.refund.domain;

import com.tpay.domains.refund.application.dto.RefundReceiptDto;

import java.util.List;

public interface RefundRepositoryCustom {
    List<RefundReceiptDto.Response> findRefundReceipt(String encryptPassportNumber, boolean refundAfter);
    List<RefundFindDto.Response> findRefundAFranchisee(Long franchiseeIndex);
}
