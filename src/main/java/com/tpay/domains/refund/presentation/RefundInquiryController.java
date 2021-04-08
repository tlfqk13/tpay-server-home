package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundInquiryService;
import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.refund.application.dto.RefundInquiryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundInquiryController {

  private final RefundInquiryService refundInquiryService;

  @PostMapping("/refund/inquiry")
  private ResponseEntity<RefundInquiryResponse> refundInquiry(
      @RequestBody RefundInquiryRequest refundInquiryRequest) {
    return ResponseEntity.ok(refundInquiryService.refundInquiry(refundInquiryRequest));
  }
}
