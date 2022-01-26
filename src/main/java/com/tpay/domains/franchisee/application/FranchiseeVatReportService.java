package com.tpay.domains.franchisee.application;


import com.tpay.domains.franchisee.application.dto.FranchiseeVatReportResponseInterface;
import com.tpay.domains.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FranchiseeVatReportService {

  private final OrderRepository orderRepository;

  public FranchiseeVatReportResponseInterface vatReport(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
    FranchiseeVatReportResponseInterface result = orderRepository.findQuarterlyVatReport(franchiseeIndex, startDate, endDate);
    return result;
  }
}
