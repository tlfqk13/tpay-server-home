package com.tpay.domains.refund_core.presentation;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.FranchiseeAuthenticationException;
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
        log.trace("Refund Cancel Start = {}", customerIndex);
        RefundResponse response = refundCancelService.cancel(customerIndex, refundIndex);
        log.trace("Refund Cancel Finish = {}", customerIndex);
        return ResponseEntity.ok(response);
    }

    // TODO: 2022/08/18 관리자가 직접 가맹점 환급 취소시 사용
    @PatchMapping("/admin-cancel/{userSelector}/{index}")
    public ResponseEntity<RefundResponse> refundCancelFromAdmin(
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
    public ResponseEntity<RefundResponse> find(
            @RequestBody RefundLimitRequest request) {
        log.trace("Refund Limit Find Start = {}", request);
        RefundResponse response = limitFindService.find(request);
        log.trace("Refund Limit Find Finish = {}", request);
        return ResponseEntity.ok(response);
    }

    // TODO: 2022/09/15 tourCash_Admin 전용 RefundApprove
    @PostMapping("/approval/tourcash/{userSelector}/{index}")
    public ResponseEntity<RefundResponse> tourCashRefundApproval(
            @RequestBody RefundSaveRequest request,
            @PathVariable UserSelector userSelector,
            @PathVariable Long index) {
        // tour_cash 가맹점 franchiseeIndex 가 아니면 예외처리
        if(request.getFranchiseeIndex() != 95L){
            throw new FranchiseeAuthenticationException(
                    ExceptionState.AUTHENTICATION_FAILED, "Token not exists");
        }
        log.trace("Refund Approval Start = {}", request);
        RefundResponse response = refundApproveService.approve(request);
        log.trace("Refund Approval Finish = {}", response);
        return ResponseEntity.ok(response);
    }
}
