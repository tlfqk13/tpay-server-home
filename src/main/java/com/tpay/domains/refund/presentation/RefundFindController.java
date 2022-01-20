package com.tpay.domains.refund.presentation;

import com.tpay.commons.util.DateFilter;
import com.tpay.domains.customer.application.dto.CustomerInfo;
import com.tpay.domains.refund.application.RefundFindService;
import com.tpay.domains.refund.application.dto.RefundFindResponse;
import com.tpay.domains.refund.application.dto.RefundFindResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

  @GetMapping("/refunds")
  public ResponseEntity<List<RefundFindResponseInterface>> findAll() {
    List<RefundFindResponseInterface> response = refundFindService.findAll();
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refunds/customer/{franchiseeIndex}")
  public ResponseEntity<List<RefundFindResponse>> findAllByCustomerInfo(@RequestParam DateFilter dateFilter,
                                                                        @PathVariable Long franchiseeIndex,
                                                                        @RequestParam(required = false) LocalDate startDate,
                                                                        @RequestParam(required = false) LocalDate endDate,
                                                                        @RequestBody CustomerInfo customerInfo) {
    List<RefundFindResponse> responseList =
        refundFindService.findAllByCustomerInfo(franchiseeIndex, customerInfo, dateFilter, startDate, endDate);
    return ResponseEntity.ok(responseList);

  }
}
