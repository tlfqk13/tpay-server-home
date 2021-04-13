package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundApprovalService;
import com.tpay.domains.refund.application.dto.RefundRegisterRequest;
import com.tpay.domains.refund.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundApprovalController {

  private final RefundApprovalService refundApprovalService;

  @PostMapping("/refund/approval")
  public ResponseEntity<RefundResponse> refundApproval(
      @RequestBody RefundRegisterRequest refundRegisterRequest) {
    return ResponseEntity.ok(refundApprovalService.refundApproval(refundRegisterRequest));
  }
}
