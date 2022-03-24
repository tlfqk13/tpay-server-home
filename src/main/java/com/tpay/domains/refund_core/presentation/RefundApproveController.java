package com.tpay.domains.refund_core.presentation;

import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund_core.application.RefundApproveService;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundApproveController {

    private final RefundApproveService refundApproveService;

    @PostMapping("/refund/approval")
    public ResponseEntity<RefundResponse> refundApproval(@RequestBody RefundSaveRequest request) {
        RefundResponse response = refundApproveService.approve(request);
        return ResponseEntity.ok(response);
    }
}
