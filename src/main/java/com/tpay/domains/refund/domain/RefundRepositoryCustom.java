package com.tpay.domains.refund.domain;

import com.tpay.domains.order.application.dto.CmsDto;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import com.tpay.domains.refund_test.dto.RefundFindAllDto;
import com.tpay.domains.refund_test.dto.RefundFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface RefundRepositoryCustom {
    List<RefundReceiptDto.Response> findRefundReceipt(String encryptPassportNumber, boolean refundAfter);
    List<RefundFindDto.Response> findRefundDetail(Long franchiseeIndex,LocalDate startLocalDate, LocalDate endLocalDate);
    Page<RefundFindAllDto.Response> findRefundAll(Pageable pageable, LocalDate startLocalDate, LocalDate endLocalDate
            , boolean isKeywordEmpty, boolean businessNumber, String searchKeyword,RefundStatus  refundStatus);
    // TODO: 2022/10/27 CMS Service
    List<CmsDto.Response> findFranchiseeIdCmsService(LocalDate start,LocalDate end);
}
