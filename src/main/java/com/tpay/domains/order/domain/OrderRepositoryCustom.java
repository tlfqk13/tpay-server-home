package com.tpay.domains.order.domain;

import com.tpay.domains.order.application.dto.CmsDetailDto;
import com.tpay.domains.vat.application.dto.VatDetailDto;
import com.tpay.domains.vat.application.dto.VatTotalDto;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepositoryCustom {

    List<VatDetailDto.Response> findMonthlyCmsVatDetail(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate, int pageData);

    VatTotalDto.Response findMonthlyTotal(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate);

    CmsDetailDto.Response findCmsBankInfo(Long franchiseeIndex);
}