package com.tpay.domains.api.controller;

import com.tpay.domains.api.domain.vo.RefundApprovalDto;
import com.tpay.domains.api.service.ApiService;
import com.tpay.domains.refund_core.application.RefundApproveService;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;
    private final RefundApproveService refundService;

    @PostMapping("/refund")
    public RefundResponse refundApprove(@RequestBody RefundApprovalDto.Request dto) {
        Long customerId = apiService.createCustomer(dto);
        return refundService.approve(customerId, dto);
    }
}
