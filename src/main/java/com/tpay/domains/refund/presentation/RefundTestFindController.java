package com.tpay.domains.refund.presentation;

import com.tpay.commons.util.DateFilter;
import com.tpay.domains.refund.application.RefundTestDetailFindService;
import com.tpay.domains.refund.application.dto.RefundByCustomerDateResponse;
import com.tpay.domains.refund.application.dto.RefundCustomerRequest;
import com.tpay.domains.refund.application.dto.RefundFindResponseInterface;
import com.tpay.domains.refund.application.dto.RefundPagingFindResponse;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class RefundTestFindController {

    private final RefundTestDetailFindService refundTestDetailFindService;

    @GetMapping("/refunds/franchisee/{franchiseeIndex}")
    public ResponseEntity<List<RefundFindResponseInterface>> findList(
        @PathVariable Long franchiseeIndex,
        @RequestParam DateFilter dateFilter,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate) {

        List<RefundFindResponseInterface> responseList =
            refundTestDetailFindService.findList(franchiseeIndex, startDate, endDate);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/refunds/franchisee/detail/{franchiseeIndex}")
    public ResponseEntity<List<RefundFindResponseInterface>> findDetail(
            @PathVariable Long franchiseeIndex,
            @RequestParam Long refundIndex) {
        List<RefundFindResponseInterface> responseDetail =
                refundTestDetailFindService.findDetail(franchiseeIndex,refundIndex);
        return ResponseEntity.ok(responseDetail);
    }

    @GetMapping("/admin/refunds")
    public ResponseEntity<RefundPagingFindResponse> findAll(
        @RequestParam int page,
        @RequestParam String startDate,
        @RequestParam String endDate,
        @RequestParam RefundStatus refundStatus,
        @RequestParam String searchKeyword
    ) {
        RefundPagingFindResponse response = refundTestDetailFindService.findAll(page,startDate, endDate, refundStatus,searchKeyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/refunds/{franchiseeIndex}")
    public ResponseEntity<List<RefundFindResponseInterface>> findAFranchisee(
            @PathVariable Long franchiseeIndex) {
        List<RefundFindResponseInterface> result = refundTestDetailFindService.findAFranchisee(franchiseeIndex);
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
