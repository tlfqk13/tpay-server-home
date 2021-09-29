package com.tpay.domains.sale.application;

import com.tpay.commons.util.DateFilter;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleAnalysisFindService {

  private final RefundRepository refundRepository;

  public List<SaleAnalysisFindResponse> findByDateRange(
      Long franchiseeIndex, DateFilter dateFilter, LocalDate startDate, LocalDate endDate) {

    if (endDate != null) {
      endDate = endDate.plusDays(1);
    }

    if (dateFilter != DateFilter.CUSTOM) {
      startDate = dateFilter.getStartDate();
      endDate = dateFilter.getEndDate();
    }

    List<SaleAnalysisFindResponse> saleAnalysisFindResponseList =
        refundRepository.findSaleAnalysis(franchiseeIndex, startDate, endDate);

    return saleAnalysisFindResponseList;
  }
}
