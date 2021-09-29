package com.tpay.domains.sale.application;

import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import com.tpay.domains.sale.domain.DateFilter;
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

    if (dateFilter != DateFilter.CUSTOM) {
      startDate = dateFilter.getStartDate();
      endDate = dateFilter.getEndDate();
    }

    List<SaleAnalysisFindResponse> saleAnalysisFindResponseList =
        refundRepository.findSaleAnalysis(franchiseeIndex, startDate, endDate);

    return saleAnalysisFindResponseList;
  }
}
