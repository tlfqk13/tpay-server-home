package com.tpay.domains.tourcash.controller;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.FranchiseeAuthenticationException;
import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.UserSelector;
import com.tpay.domains.refund_core.application.LimitFindService;
import com.tpay.domains.refund_core.application.RefundApproveService;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import com.tpay.domains.tourcash.dto.TourcashRefundApproveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/tourcash")
@RestController
@RequiredArgsConstructor
public class TourcashController {

    private final RefundApproveService refundApproveService;
    private final LimitFindService limitFindService;

    @GetMapping("/refund/limit")
    public ResponseEntity<RefundResponse> limit(
            @RequestBody RefundLimitRequest request) {
        RefundResponse response = limitFindService.find(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refund/approval")
    public ResponseEntity<RefundResponse> approve(@RequestBody TourcashRefundApproveDto dto) {

        if (dto.getFranchiseeIndex() != 95L) {
            throw new FranchiseeAuthenticationException(
                    ExceptionState.AUTHENTICATION_FAILED, "Token not exists");
        }

        RefundResponse response = refundApproveService.approve(dto.convertTo()
                , new IndexInfo(UserSelector.FRANCHISEE, dto.getFranchiseeIndex()));

        return ResponseEntity.ok(response);
    }
}
