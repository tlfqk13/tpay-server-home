package com.tpay.domains.erp.test.controller;

import com.tpay.domains.customer.application.dto.DepartureStatus;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.erp.test.service.ErpRefundDetailFindTestService;
import com.tpay.domains.refund.application.dto.RefundDetailTotalDto;
import com.tpay.domains.refund.application.dto.RefundFindAllDto;
import com.tpay.domains.refund.application.dto.RefundPaymentDto;
import com.tpay.domains.refund.domain.PaymentStatus;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test/admin/refunds")
@Slf4j
public class ErpRefundFindTestController {
    private final ErpRefundDetailFindTestService refundTestDetailFindService;

    // 2022/10/26 환급 현황 All
    @GetMapping
    public ResponseEntity<Page<RefundFindAllDto.Response>> findAll(
            Pageable pageable,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String searchKeyword,
            @RequestParam RefundType refundType,
            @RequestParam RefundStatus refundStatus,
            @RequestParam DepartureStatus departureStatus,
            @RequestParam PaymentStatus paymentStatus
            ) {

        Page<RefundFindAllDto.Response> response = refundTestDetailFindService.findAll(pageable, startDate, endDate, searchKeyword
                , refundType, refundStatus, departureStatus, paymentStatus);
        return ResponseEntity.ok(response);
    }
    // 2022/10/26  가맹점현황 > 환급현황
    @GetMapping("/{franchiseeIndex}")
    public ResponseEntity<RefundDetailTotalDto.Response> findRefundDetail(
            @PathVariable Long franchiseeIndex,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        RefundDetailTotalDto.Response result = refundTestDetailFindService.findRefundDetail(franchiseeIndex, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail/{refundIndex}")
    public ResponseEntity<RefundPaymentDto.Response> findPaymentDetail(
            @PathVariable Long refundIndex
    ) {
        RefundPaymentDto.Response result = refundTestDetailFindService.findPaymentDetail(refundIndex);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/detail/{refundIndex}")
    public ResponseEntity<String> registerPaymentDetail(
            @PathVariable Long refundIndex,
            @RequestBody RefundPaymentDto.Request request
    ) {
        refundTestDetailFindService.registerPaymentDetail(refundIndex,request);
        return ResponseEntity.ok("registerPaymentDetail");
    }

    @PatchMapping("/detail/{refundIndex}")
    public ResponseEntity<String> updatePaymentDetail(
            @PathVariable Long refundIndex,
            @RequestBody RefundPaymentDto.Request request
            ) {
        refundTestDetailFindService.updatePaymentDetail(refundIndex,request);
        return ResponseEntity.ok("updatePaymentDetail");
    }
}
