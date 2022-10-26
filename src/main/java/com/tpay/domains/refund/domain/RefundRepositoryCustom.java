package com.tpay.domains.refund.domain;

import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import com.tpay.domains.refund_test.dto.RefundFindDto;

import java.util.List;

public interface RefundRepositoryCustom {
    List<RefundReceiptDto.Response> findRefundReceipt(String encryptPassportNumber, boolean refundAfter);
    List<RefundFindDto.Response> findRefundAFranchisee(Long franchiseeIndex);
}
