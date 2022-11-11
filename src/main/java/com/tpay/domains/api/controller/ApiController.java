package com.tpay.domains.api.controller;

import com.tpay.domains.api.domain.vo.ApprovalDto;
import com.tpay.domains.api.domain.vo.CancelDto;
import com.tpay.domains.api.service.ApiService;
import com.tpay.domains.refund_core.application.RefundApproveService;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;
    private final RefundApproveService refundService;

    @PostMapping("/refund")
    public RefundResponse approve(@RequestBody ApprovalDto.Request dto) {
        Long customerId = apiService.createCustomer(dto);
        return refundService.approve(customerId, dto);
    }

    @PostMapping("/refund/cancel")
    public void cancel(@RequestBody CancelDto.Request dto) {
        apiService.cancelRefund(dto.getPurchaseSequenceNumber());
    }

    @PostMapping("/refund/cancel/bulk")
    public List<RefundResponse>  bulkCancel(@RequestBody List<CancelDto.Request> dtos) {
        return apiService.cancelRefund(dtos);
    }
}
