package com.tpay.domains.sale.application;


import com.tpay.commons.util.DateFilterV2;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleAnalysisFindService {

  private final RefundRepository refundRepository;

  public List<SaleAnalysisFindResponseInterface> findByDateRange(
      Long franchiseeIndex, DateFilterV2 dateFilterV2, String startDate, String endDate) {
    String startDateQuery;
    String endDateQuery;
    if (dateFilterV2.equals(DateFilterV2.CUSTOM)) {
      startDateQuery = startDate.replaceAll("-", "");
      endDateQuery = endDate.replaceAll("-", "");
    } else {
      startDateQuery = dateFilterV2.getStartDate().replaceAll("-", "");
      endDateQuery = dateFilterV2.getEndDate().replaceAll("-", "");
    }

    List<SaleAnalysisFindResponseInterface> saleAnalysisFindResponseInterfaceList =
        refundRepository.findSaleAnalysisV2(franchiseeIndex, startDateQuery, endDateQuery);

    return saleAnalysisFindResponseInterfaceList;
  }

  //테스트용 메서드
  public List<String> testDateNativeQuery(
      Long franchiseeIndex, DateFilterV2 dateFilterV2, String startDate, String endDate) {
    String startDateQueryTest;
    String endDateQueryTest;
    if (dateFilterV2.equals(DateFilterV2.CUSTOM)) {
      startDateQueryTest = startDate.replaceAll("-", "");
      endDateQueryTest = endDate.replaceAll("-", "");
    } else {
      startDateQueryTest = dateFilterV2.getStartDate().replaceAll("-", "");
      endDateQueryTest = dateFilterV2.getEndDate().replaceAll("-", "");
    }
    return refundRepository.dateTestNativeQuery(franchiseeIndex, startDateQueryTest, endDateQueryTest);
  }
}
