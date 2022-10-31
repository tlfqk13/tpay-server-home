package com.tpay.domains.refund_test.presentation;

import com.tpay.domains.refund.application.dto.RefundByCustomerDateResponse;
import com.tpay.domains.refund.application.dto.RefundCustomerRequest;
import com.tpay.domains.refund.application.dto.RefundFindResponseInterface;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.refund_test.application.RefundTestDetailFindService;
import com.tpay.domains.refund_test.dto.RefundDetailTotalDto;
import com.tpay.domains.refund_test.dto.RefundTestPagingFindResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
@Slf4j
public class RefundTestFindController {
    private final RefundTestDetailFindService refundTestDetailFindService;

    // TODO: 2022/10/27 가맹점현황 > 환급현황 - 상세보기 > 기간선택
    @GetMapping("/refunds/franchisee/{franchiseeIndex}")
    public ResponseEntity<List<RefundFindResponseInterface>> findList(
        @PathVariable Long franchiseeIndex,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate) {

        List<RefundFindResponseInterface> responseList =
            refundTestDetailFindService.findList(franchiseeIndex, startDate, endDate);
        return ResponseEntity.ok(responseList);
    }

    // TODO: 2022/10/26 환급 현황 All
    @GetMapping("/admin/refunds")
    public ResponseEntity<RefundTestPagingFindResponse> findAll(
            @RequestParam int page,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam RefundStatus refundStatus,
            @RequestParam String searchKeyword
    ) {
        RefundTestPagingFindResponse response = refundTestDetailFindService.findAll(page,refundStatus,startDate, endDate,searchKeyword);
        return ResponseEntity.ok(response);
    }

    // TODO: 2022/10/26  가맹점현황 > 환급현황
    @GetMapping("/admin/refunds/{franchiseeIndex}")
    public ResponseEntity<RefundDetailTotalDto.Response> findRefundDetail(
            @PathVariable Long franchiseeIndex,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        RefundDetailTotalDto.Response result = refundTestDetailFindService.findRefundDetail(franchiseeIndex, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refunds/customer/{franchiseeIndex}")
    public ResponseEntity<List<RefundByCustomerDateResponse>> findRefundsByCustomerInfo(
        @PathVariable Long franchiseeIndex,
        @RequestBody RefundCustomerRequest refundCustomerRequest
    ) {
        List<RefundByCustomerDateResponse> result = refundTestDetailFindService.findRefundsByCustomerInfo(franchiseeIndex, refundCustomerRequest);
        return ResponseEntity.ok(result);
    }
}
