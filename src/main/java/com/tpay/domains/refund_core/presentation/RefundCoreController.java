package com.tpay.domains.refund_core.presentation;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund_core.application.LimitFindService;
import com.tpay.domains.refund_core.application.RefundApproveService;
import com.tpay.domains.refund_core.application.RefundCancelService;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 실질적인 관세청 통신 3개 기능
 * 한도조회, 승인, 취소
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/refund")
public class RefundCoreController {

    private final RefundApproveService refundApproveService;
    private final RefundCancelService refundCancelService;
    private final LimitFindService limitFindService;

    /**
     * 환급 승인 요청
     * URL에 있는 userSelector와 index는 jwt 추가검증 때문에 삽입. 서비스단에 사용되진 않음
     */
    @PostMapping("/approval/{userSelector}/{index}")
    public ResponseEntity<RefundResponse> refundApproval(
        @RequestBody RefundSaveRequest request,
        @PathVariable UserSelector userSelector,
        @PathVariable Long index) {
        log.trace("Refund Approval Start = {}", request);
        RefundResponse response = refundApproveService.approve(request);
        log.trace("Refund Approval Finish = {}", response);
        return ResponseEntity.ok(response);
    }


    /**
     * 환급 취소 요청
     * URL에 있는 userSelector와 index는 jwt 추가검증 때문에 삽입. 서비스단에 사용되진 않음
     */
    @PatchMapping("/cancel/{userSelector}/{index}")
    public ResponseEntity<RefundResponse> refundCancel(
        @RequestParam Long customerIndex, @RequestParam Long refundIndex,
        @PathVariable UserSelector userSelector,
        @PathVariable Long index
    ) {
        RefundResponse response = refundCancelService.cancel(customerIndex, refundIndex);
        return ResponseEntity.ok(response);
    }

    /**
     * 한도조회
     */
    @PostMapping("/limit")
    private ResponseEntity<RefundResponse> find(
        @RequestBody RefundLimitRequest request) {
        log.trace("Refund Limit Find Start = {}", request);
        RefundResponse response = limitFindService.find(request);
        log.trace("Refund Limit Find Finish = {}", request);
        return ResponseEntity.ok(response);
    }
}
