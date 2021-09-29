package com.tpay.domains.refund.presentation;

import com.tpay.commons.util.DateFilter;
import com.tpay.domains.refund.application.RefundFindService;
import com.tpay.domains.refund.application.dto.RefundFindResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RefundFindController {

  private final RefundFindService refundFindService;

  @GetMapping("/refunds/franchisee/{franchiseeIndex}")
  public ResponseEntity<List<RefundFindResponse>> findList(
      @PathVariable Long franchiseeIndex,
      @RequestParam DateFilter dateFilter,
      @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate) {

    List<RefundFindResponse> responseList =
        refundFindService.findList(franchiseeIndex, dateFilter, startDate, endDate);
    return ResponseEntity.ok(responseList);
  }
}
