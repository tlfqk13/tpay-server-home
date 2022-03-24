package com.tpay.domains.refund_core.presentation;

import com.tpay.domains.refund_core.application.LimitFindService;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
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
    private ResponseEntity<RefundResponse> find(
        @RequestBody RefundLimitRequest request) {
        RefundResponse response = limitFindService.find(request);
        return ResponseEntity.ok(response);
    }
}
