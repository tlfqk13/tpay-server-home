package com.tpay.domains.sale.presentation;

import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.sale.application.SaleFindService;
import com.tpay.domains.sale.application.dto.SaleFindResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SaleFindController {
  private final SaleFindService saleFindService;

  @PostMapping("/refund")
  public ResponseEntity<List<SaleFindResponse>> findAll(
      @RequestBody RefundInquiryRequest refundInquiryRequest) {
    return ResponseEntity.ok(saleFindService.findAllSale(refundInquiryRequest));
  }
}
