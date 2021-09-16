package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.LimitFindService;
import com.tpay.domains.refund.application.dto.RefundLimitRequest;
import com.tpay.domains.refund.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LimitFindController {

  private final LimitFindService limitFindService;

  @PostMapping("/refund/limit")
  private ResponseEntity<RefundResponse> refundInquiry(
      @RequestBody RefundLimitRequest request) {
    RefundResponse response = limitFindService.refundInquiry(request);
    return ResponseEntity.ok(response);
  }
}
