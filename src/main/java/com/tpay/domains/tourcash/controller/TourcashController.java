package com.tpay.domains.tourcash.controller;

import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.UserSelector;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import com.tpay.domains.tourcash.application.TourcashRefundService;
import com.tpay.domains.tourcash.dto.TourcashRefundApproveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/tourcash")
@RestController
@RequiredArgsConstructor
public class TourcashController {

    private final TourcashRefundService tourcashRefundService;

    @PostMapping("/refund/approval")
    public ResponseEntity<RefundResponse> approve(@RequestBody TourcashRefundApproveDto dto) {
     /*   if (dto.getFranchiseeIndex() != 95L) {
            throw new FranchiseeAuthenticationException(
                    ExceptionState.AUTHENTICATION_FAILED, "Token not exists");
        }*/

        RefundResponse response = tourcashRefundService.approve(dto.convertTo()
                , new IndexInfo(UserSelector.FRANCHISEE, dto.getFranchiseeIndex()));

        return ResponseEntity.ok(response);
    }
}
