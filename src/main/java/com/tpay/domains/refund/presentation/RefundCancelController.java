package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundCancelService;
import com.tpay.domains.refund.application.dto.RefundCancelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RefundCancelController {

  private final RefundCancelService refundCancelService;

  @PutMapping("/refund/{userIndex}/cancel/{refundIndex}")
  public ResponseEntity<RefundCancelResponse> refundCancel(
      @PathVariable Long userIndex, @PathVariable Long refundIndex) {
    return ResponseEntity.ok(refundCancelService.refundCancel(userIndex, refundIndex));
  }
}
