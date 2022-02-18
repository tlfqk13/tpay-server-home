package com.tpay.domains.sale.application;


import com.tpay.commons.util.DateFilterV2;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleStatisticsService {

  private final RefundRepository refundRepository;

  public SaleStatisticsResponseInterface saleStatistics(Long franchiseeIndex, DateFilterV2 dateFilter){
    String startDate = dateFilter.getStartDate();
    String endDate = dateFilter.getEndDate();
    return refundRepository.findStatistics(franchiseeIndex, startDate, endDate);
  }
}
