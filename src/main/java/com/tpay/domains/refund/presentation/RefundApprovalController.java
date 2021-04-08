package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundApprovalService;
import com.tpay.domains.refund.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundApprovalController {

  private final RefundApprovalService refundApprovalService;

  @PostMapping("/{franchiseeIndex}/refund/{customerIndex}/approval/{price}")
  public ResponseEntity<RefundResponse> refundApproval(
      @PathVariable Long franchiseeIndex,
      @PathVariable Long customerIndex,
      @PathVariable String price) {
    return ResponseEntity.ok(
        refundApprovalService.refundApproval(franchiseeIndex, customerIndex, price));
  }
}
