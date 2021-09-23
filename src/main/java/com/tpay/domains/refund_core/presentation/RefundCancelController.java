package com.tpay.domains.refund_core.presentation;

import com.tpay.domains.refund_core.application.RefundCancelService;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
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
  public ResponseEntity<RefundResponse> refundCancel(
      @PathVariable Long userIndex, @PathVariable Long refundIndex) {
    return ResponseEntity.ok(refundCancelService.refundCancel(userIndex, refundIndex));
  }
}
