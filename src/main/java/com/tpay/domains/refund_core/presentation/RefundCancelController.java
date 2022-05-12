package com.tpay.domains.refund_core.presentation;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.refund_core.application.RefundCancelService;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RefundCancelController {

    private final RefundCancelService refundCancelService;

    /**
     * 환급 취소 요청
     * URL에 있는 userSelector와 index는 jwt 추가검증 때문에 삽입. 서비스단에 사용되진 않음
     */
    @PatchMapping("/refund/cancel/{userSelector}/{index}")
    public ResponseEntity<RefundResponse> refundCancel(
        @RequestParam Long customerIndex, @RequestParam Long refundIndex,
        @PathVariable UserSelector userSelector,
        @PathVariable Long index
    ) {
        RefundResponse response = refundCancelService.cancel(customerIndex, refundIndex);
        return ResponseEntity.ok(response);
    }
}
