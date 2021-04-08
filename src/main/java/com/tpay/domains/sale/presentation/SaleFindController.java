package com.tpay.domains.sale.presentation;

import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.sale.application.SaleFindService;
import com.tpay.domains.sale.application.dto.SaleFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SaleFindController {
  private final SaleFindService saleFindService;

  @GetMapping("/refund")
  public ResponseEntity<List<SaleFindResponse>> findAll(
      @RequestBody RefundInquiryRequest refundInquiryRequest) {
    return ResponseEntity.ok(saleFindService.findAllSale(refundInquiryRequest));
  }
}
