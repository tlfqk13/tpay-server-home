package com.tpay.domains.order.domain;

import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.order.application.dto.CmsDetailDto;
import com.tpay.domains.vat.application.dto.HometaxTailDto;
import com.tpay.domains.vat.application.dto.VatDetailDto;
import com.tpay.domains.vat.application.dto.VatTotalDto;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepositoryCustom {

    List<VatDetailDto.Response> findMonthlyCmsVatDetail(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate, int pageData, RefundType refundType);

    VatTotalDto.Response findMonthlyTotal(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate, RefundType refundType);

    CmsDetailDto.Response findCmsBankInfo(Long franchiseeIndex);

    List<OrderEntity> findRefundAfterOrdersBetweenDates(Long franchiseeIndex, LocalDate startDate, LocalDate endDate);

    HometaxTailDto findRefundAfterOrdersTotalBetweenDates(Long franchiseeIndex, LocalDate startDate, LocalDate endDate);

    VatTotalDto.Response findMonthlyTotal(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate, boolean isCms);

    List<VatDetailDto.Response> findMonthlyCmsVatDetail(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate, int pageData, boolean isCms);
}
