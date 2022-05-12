package com.tpay.domains.refund_core.presentation;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund_core.application.RefundApproveService;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundApproveController {

    private final RefundApproveService refundApproveService;

    /**
     * 환급 승인 요청
     * URL에 있는 userSelector와 index는 jwt 추가검증 때문에 삽입. 서비스단에 사용되진 않음
     */
    @PostMapping("/refund/approval/{userSelector}/{index}")
    public ResponseEntity<RefundResponse> refundApproval(
        @RequestBody RefundSaveRequest request,
        @PathVariable UserSelector userSelector,
        @PathVariable Long index) {
        RefundResponse response = refundApproveService.approve(request);
        return ResponseEntity.ok(response);
    }
}
