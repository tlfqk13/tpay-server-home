package com.tpay.domains.order.presentation;

import com.tpay.domains.order.application.OrderAnalysisService;
import com.tpay.domains.order.application.dto.OrderFindResponse;
import com.tpay.domains.order.application.dto.OrderAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderAnalysisController {

  private final OrderAnalysisService orderAnalysisService;

  @GetMapping("/{franchiseeIndex}/analysis")
  public ResponseEntity<List<OrderAnalysisResponse>> findSale(
      @PathVariable Long franchiseeIndex, @RequestParam String period) {
    return ResponseEntity.ok(orderAnalysisService.findUnit(franchiseeIndex, period));
  }

  @GetMapping("/{franchiseeIndex}/analysis/period")
  public ResponseEntity<List<OrderAnalysisResponse>> findAllSale(
      @PathVariable Long franchiseeIndex,
      @RequestParam String startDate,
      @RequestParam String endDate) {
    return ResponseEntity.ok(orderAnalysisService.findPeriod(franchiseeIndex, startDate, endDate));
  }

  @GetMapping("/{franchiseeIndex}/{saleDate}")
  public ResponseEntity<List<OrderFindResponse>> findOneSale(@PathVariable Long franchiseeIndex, @PathVariable String saleDate){
    return ResponseEntity.ok(orderAnalysisService.findOneSale(franchiseeIndex,saleDate));
  }

}
