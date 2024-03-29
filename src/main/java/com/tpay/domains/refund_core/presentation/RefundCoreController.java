package com.tpay.domains.refund_core.presentation;

import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.order.application.dto.OrderDto;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund_core.application.LimitFindService;
import com.tpay.domains.refund_core.application.RefundApproveService;
import com.tpay.domains.refund_core.application.RefundCancelService;
import com.tpay.domains.refund_core.application.dto.RefundAfterCancelDto;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
     */
    @PostMapping("/approval")
    public ResponseEntity<RefundResponse> refundApproval(
            @RequestBody RefundSaveRequest request,
            @KtpIndexInfo IndexInfo indexInfo) {
        log.debug("Refund Approval Start = {}", request);
        RefundResponse response = refundApproveService.approve(request, indexInfo);
        log.debug("Refund Approval Finish = {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * 환급 취소 요청
     */
    @PatchMapping("/cancel")
    public ResponseEntity<RefundResponse> refundCancel(
            @RequestParam Long customerIndex,
            @RequestParam Long refundIndex
    ) {
        log.debug("Refund Cancel Start = {}", customerIndex);
        RefundResponse response = refundCancelService.cancel(customerIndex, refundIndex);
        log.debug("Refund Cancel Finish = {}", customerIndex);
        return ResponseEntity.ok(response);
    }

    // 관리자가 직접 가맹점 환급 취소시 사용
    @PatchMapping("/admin-cancel")
    public ResponseEntity<RefundResponse> refundCancelFromAdmin(
            @RequestParam Long customerIndex,
            @RequestParam Long refundIndex
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
        log.debug("Refund Limit Find Start = {}", request);
        RefundResponse response = limitFindService.find(request);
        log.debug("Refund Limit Find Finish = {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * 사후 환급 승인
     */
    @PostMapping("/after/approval")
    public ResponseEntity<RefundResponse> refundAfterApproval(
            HttpServletRequest request,
            @RequestBody OrderDto.Request dto,
            @KtpIndexInfo IndexInfo indexInfo) {
        return ResponseEntity.ok(refundApproveService.approveAfter(dto, indexInfo));
    }

    /**
     * 사후 환급 확인
     */
    @GetMapping("/after/approval/{purchaseSn}")
    public ResponseEntity<RefundResponse> refundAfterApprovalConfirm(
            HttpServletRequest request,
            @KtpIndexInfo IndexInfo indexInfo, @PathVariable String purchaseSn) {
        return ResponseEntity.ok(refundApproveService.confirmApproveAfter(purchaseSn));
    }

    /**
     * 사후 환급 취소
     */
    @PostMapping("/after/cancel")
    public ResponseEntity<String> cancelRefundAfter(
            @RequestBody RefundAfterCancelDto.Request request) {
        refundCancelService.cancelRefundAfter(request.getTkOutNum());
        return ResponseEntity.ok(request.getTkOutNum());
    }
}
