package com.tpay.domains.franchisee.presentation;


import com.tpay.domains.franchisee.application.FranchiseeVatReportService;
import com.tpay.domains.franchisee.application.dto.FranchiseeVatReportResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class FranchiseeVatReportController {

  private final FranchiseeVatReportService franchiseeVatReportService;


  @GetMapping("/franchisee/{franchiseeIndex}/vat")
  public ResponseEntity<FranchiseeVatReportResponseInterface> vatReport(
      @PathVariable Long franchiseeIndex,
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate
  ) {
    FranchiseeVatReportResponseInterface result = franchiseeVatReportService.vatReport(franchiseeIndex, startDate, endDate);
    return ResponseEntity.ok(result);
  }


}
