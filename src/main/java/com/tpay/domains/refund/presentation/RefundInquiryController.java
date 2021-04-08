package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundInquiryService;
import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.refund.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundInquiryController {

  private final RefundInquiryService refundInquiryService;

  @GetMapping("/refund/inquiry")
  private ResponseEntity<RefundResponse> refundInquiry(RefundInquiryRequest refundInquiryRequest) {
    return ResponseEntity.ok(refundInquiryService.refundInquiry(refundInquiryRequest));
  }
}
