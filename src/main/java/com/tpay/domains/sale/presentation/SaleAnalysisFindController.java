package com.tpay.domains.sale.presentation;

import com.tpay.domains.sale.application.SaleAnalysisFindService;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import com.tpay.commons.util.DateFilter;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SaleAnalysisFindController {

  private final SaleAnalysisFindService saleAnalysisFindService;

  @GetMapping("/sales/franchisee/{franchiseeIndex}")
  public ResponseEntity<List<SaleAnalysisFindResponse>> findByDateRange(
      @PathVariable Long franchiseeIndex,
      @RequestParam DateFilter dateFilter,
      @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate) {

    List<SaleAnalysisFindResponse> responseList =
        saleAnalysisFindService.findByDateRange(franchiseeIndex, dateFilter, startDate, endDate);

    return ResponseEntity.ok(responseList);
  }
}
