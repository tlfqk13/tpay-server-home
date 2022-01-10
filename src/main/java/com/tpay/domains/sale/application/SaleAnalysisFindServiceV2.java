package com.tpay.domains.sale.application;


import com.tpay.commons.util.DateFilterV2;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleAnalysisFindServiceV2 {

  private final RefundRepository refundRepository;

  public List<SaleAnalysisFindResponse> findByDateRange(
      Long franchiseeIndex, DateFilterV2 dateFilterV2) {

    String startDate = dateFilterV2.getStartDate().replaceAll("-","");
    String endDate = dateFilterV2.getEndDate().replaceAll("-","");
    System.out.println("=====V2 Logger=====");
    System.out.println("startDate : " + startDate);
    System.out.println("endDate : " + endDate);
    System.out.println("=====V2 Logger=====");
    List<SaleAnalysisFindResponse> saleAnalysisFindResponseList =
        refundRepository.findSaleAnalysisV2(franchiseeIndex, startDate, endDate);

    return saleAnalysisFindResponseList;
  }
}
