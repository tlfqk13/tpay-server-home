package com.tpay.domains.erp.test.controller;

import com.tpay.domains.erp.test.dto.RefundTestPagingFindResponse;
import com.tpay.domains.erp.test.service.ErpRefundDetailFindTestService;
import com.tpay.domains.refund.application.dto.RefundDetailTotalDto;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<RefundTestPagingFindResponse> findAll(
            @RequestParam int page,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam RefundStatus refundStatus,
            @RequestParam String searchKeyword
    ) {
        RefundTestPagingFindResponse response = refundTestDetailFindService.findAll(page,refundStatus,startDate, endDate,searchKeyword,false);
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
}
