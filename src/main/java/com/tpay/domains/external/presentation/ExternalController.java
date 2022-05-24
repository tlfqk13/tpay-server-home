package com.tpay.domains.external.presentation;

import com.tpay.domains.external.application.ExternalRefundApprovalService;
import com.tpay.domains.external.application.ExternalRefundCancelService;
import com.tpay.domains.external.application.ExternalService;
import com.tpay.domains.external.application.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 외부 통신 서버 관련
 */
@RestController
@RequestMapping("/external/refund")
@RequiredArgsConstructor
public class ExternalController {

    private final ExternalService externalService;
    private final ExternalRefundApprovalService externalRefundApprovalService;
    private final ExternalRefundCancelService externalRefundCancelService;


    /**
     * 환급 확정
     */
    @PatchMapping("/confirm")
    public ResponseEntity<ExternalRefundResponse> statusUpdate(
        @RequestBody ExternalStatusUpdateDto externalStatusUpdateDto) {
        ExternalRefundResponse externalRefundResponse = externalService.statusUpdate(externalStatusUpdateDto);
        return ResponseEntity.ok(externalRefundResponse);
    }

    /**
     * 환급 승인
     */
    @PostMapping("/approval")
    public ResponseEntity<ExternalRefundResponse> externalRefundApproval(
        @RequestBody ExternalRefundApprovalRequest externalRefundApprovalRequest) {

        ExternalRefundResponse approve = externalRefundApprovalService.approve(externalRefundApprovalRequest);
        return ResponseEntity.ok(approve);
    }

    /**
     * 환급 취소
     */
    @PostMapping("/cancel")
    public ResponseEntity<ExternalRefundResponse> externalRefundCancel(@RequestBody ExternalRefundCancelRequest externalRefundCancelRequest) {

        ExternalRefundResponse result = externalRefundCancelService.cancel(externalRefundCancelRequest);
        return ResponseEntity.ok(result);

    }

    // 바코드 스캔 이후 자동으로 화면 넘어갈 때 사용되는 주기적인 요청
    // 현재 미사용
    @GetMapping("/result/{externalRefundIndex}")
    public ResponseEntity<ExternalStatusRequestResponse> statusRequest(@PathVariable Long externalRefundIndex) {
        return externalService.statusRequest(externalRefundIndex);
    }
}
