package com.tpay.domains.refund_core.presentation;

import com.tpay.domains.refund_core.application.RefundCancelService;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RefundCancelController {

    private final RefundCancelService refundCancelService;

    @PatchMapping("/refund/cancel")
    public ResponseEntity<RefundResponse> refundCancel(
        @RequestParam Long customerIndex, @RequestParam Long refundIndex) {
        RefundResponse response = refundCancelService.cancel(customerIndex, refundIndex);
        return ResponseEntity.ok(response);
    }
}
