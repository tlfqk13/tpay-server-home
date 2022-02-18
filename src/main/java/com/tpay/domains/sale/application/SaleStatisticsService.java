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

  public SaleStatisticsResponseInterface saleStatistics(Long franchiseeIndex, String startDate, String endDate){
    return refundRepository.findStatistics(franchiseeIndex, startDate, endDate);
  }
}
