package com.tpay.domains.sale.presentation;


import com.tpay.commons.util.DateFilterV2;
import com.tpay.commons.util.DateSelector;
import com.tpay.domains.sale.application.SaleStatisticsService;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SaleStatisticsController {

  private final SaleStatisticsService saleStatisticsService;

  @GetMapping("/sales/statistics/{franchiseeIndex}")
  public ResponseEntity<SaleStatisticsResponseInterface> salesStatistics(
      @PathVariable Long franchiseeIndex,
      @RequestParam String startDate,
      @RequestParam String endDate
  ) {
    SaleStatisticsResponseInterface result = saleStatisticsService.saleStatistics(franchiseeIndex, startDate, endDate);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/sales/statistics/detail/{franchiseeIndex}")
  public ResponseEntity<List<SaleStatisticsResponseInterface>> saleCompareMonth(
      @PathVariable Long franchiseeIndex,
      @RequestParam String startDate,
      @RequestParam String endDate,
      @RequestParam DateSelector dateSelector
      ){
    List<SaleStatisticsResponseInterface> saleStatisticsResponseInterfaceList = saleStatisticsService.saleCompare(franchiseeIndex, startDate, endDate, dateSelector);
    return ResponseEntity.ok(saleStatisticsResponseInterfaceList);
  }

}
