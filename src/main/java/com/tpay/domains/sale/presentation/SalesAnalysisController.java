package com.tpay.domains.sale.presentation;

import com.tpay.domains.sale.application.SalesAnalysisService;
import com.tpay.domains.sale.application.dto.SaleFindResponse;
import com.tpay.domains.sale.application.dto.SalesAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SalesAnalysisController {

  private final SalesAnalysisService salesAnalysisService;

  @GetMapping("/{franchiseeIndex}/analysis")
  public ResponseEntity<List<SalesAnalysisResponse>> findSale(
      @PathVariable Long franchiseeIndex, @RequestParam String period) {
    return ResponseEntity.ok(salesAnalysisService.findUnit(franchiseeIndex, period));
  }

  @GetMapping("/{franchiseeIndex}/analysis/period")
  public ResponseEntity<List<SalesAnalysisResponse>> findAllSale(
      @PathVariable Long franchiseeIndex,
      @RequestParam String startDate,
      @RequestParam String endDate) {
    return ResponseEntity.ok(salesAnalysisService.findPeriod(franchiseeIndex, startDate, endDate));
  }

  @GetMapping("/{franchiseeIndex}/{saleDate}")
  public ResponseEntity<List<SaleFindResponse>> findOneSale(@PathVariable Long franchiseeIndex, @PathVariable String saleDate){
    return ResponseEntity.ok(salesAnalysisService.findOneSale(franchiseeIndex,saleDate));
  }

}
