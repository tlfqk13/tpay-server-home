package com.tpay.domains.sale.application;

import com.tpay.commons.util.DateFilter;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
    List<SaleAnalysisFindResponse> saleAnalysisFindResponseList =
        refundRepository.findSaleAnalysis(franchiseeIndex, startDateTime, endDateTime);

    return saleAnalysisFindResponseList;
  }
}
