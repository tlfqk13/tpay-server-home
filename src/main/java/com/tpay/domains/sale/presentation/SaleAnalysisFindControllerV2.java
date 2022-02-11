package com.tpay.domains.sale.presentation;


import com.tpay.commons.util.DateFilterV2;
import com.tpay.domains.sale.application.SaleAnalysisFindServiceV2;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SaleAnalysisFindControllerV2 {

  private final SaleAnalysisFindServiceV2 saleAnalysisFindServiceV2;

  @GetMapping("/sales/franchiseeV2/{franchiseeIndex}")
  public ResponseEntity<List<SaleAnalysisFindResponse>> findByDateRange(
      @PathVariable Long franchiseeIndex,
      @RequestParam DateFilterV2 dateFilter,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate
      ) {
    List<SaleAnalysisFindResponse> result = saleAnalysisFindServiceV2.findByDateRange(franchiseeIndex, dateFilter);
    return ResponseEntity.ok(result);
  }
}
