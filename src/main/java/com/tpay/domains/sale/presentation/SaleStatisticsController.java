package com.tpay.domains.sale.presentation;


import com.tpay.commons.util.DateSelector;
import com.tpay.domains.sale.application.SaleStatisticsService;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponse;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SaleStatisticsController {

  private final SaleStatisticsService saleStatisticsService;

  @GetMapping("/sales/statistics/{franchiseeIndex}")
  public ResponseEntity<SaleStatisticsResponseInterface> salesStatistics(
      @PathVariable Long franchiseeIndex,
      @RequestParam String targetDate,
      @RequestParam DateSelector dateSelector
  ) {
    SaleStatisticsResponseInterface result = saleStatisticsService.saleStatistics(franchiseeIndex, targetDate, dateSelector);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/sales/statistics/detail/{franchiseeIndex}")
  public ResponseEntity<SaleStatisticsResponse> saleCompare(
      @PathVariable Long franchiseeIndex,
      @RequestParam String targetDate,
      @RequestParam DateSelector dateSelector
  ) {
    SaleStatisticsResponse result = saleStatisticsService.saleCompare(franchiseeIndex, targetDate, dateSelector);
    return ResponseEntity.ok(result);
  }

}