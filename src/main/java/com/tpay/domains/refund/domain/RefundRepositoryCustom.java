package com.tpay.domains.refund.domain;

import com.tpay.domains.customer.application.dto.DepartureStatus;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.order.application.dto.CmsDto;
import com.tpay.domains.refund.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface RefundRepositoryCustom {
    List<RefundReceiptDto.Response> findRefundReceipt(String encryptPassportNumber, boolean refundAfter);
    List<RefundReceiptDto.Response> downloadsRefundReceipt(String encryptPassportNumber, boolean refundAfter);
    List<RefundFindDto.Response> findRefundDetail(Long franchiseeIndex,LocalDate startLocalDate, LocalDate endLocalDate);
    Page<RefundFindAllDto.Response> findRefundAll(Pageable pageable, LocalDate startLocalDate, LocalDate endLocalDate
            , boolean isKeywordEmpty, boolean businessNumber, String searchKeyword, RefundStatus  refundStatus
            , RefundType refundType, DepartureStatus departureStatus, PaymentStatus paymentStatus);
    List<CmsDto.Response> findFranchiseeIdCmsService(LocalDate start,LocalDate end);
    List<CmsDto.Response> findFranchiseeIdAfter(LocalDate start,LocalDate end);
    RefundDetailDto.Response findRefundDetail(Long refundIndex);
    RefundPaymentDetailDto.Response findRefundPaymentDetail(Long refundIndex);
}
