package com.tpay.domains.testAPI.refund;


import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
public class RefundApproveControllerFake {

    private final FranchiseeFindService franchiseeFindService;

    @PostMapping("/refund/approval/fake")
    @Transactional
    public ResponseEntity<RefundResponse> refundApprovalFake(@RequestBody RefundSaveRequest request) {
        RefundResponse response =
            RefundResponse.builder()
                .responseCode("8888")
                .message("Execute fake approval")
                .purchaseSequenceNumber("122122")
                .takeoutNumber("1324")
                .beforeDeduction("1324")
                .afterDeduction("1324")
                .customerIndex(request.getCustomerIndex())
                .build();
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(request.getFranchiseeIndex());
        franchiseeEntity.isRefundOnce();
        return ResponseEntity.ok(response);
    }
}
