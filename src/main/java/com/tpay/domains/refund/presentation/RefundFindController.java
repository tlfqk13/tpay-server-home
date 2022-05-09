package com.tpay.domains.refund.presentation;

import com.tpay.commons.util.DateFilter;
import com.tpay.domains.refund.application.RefundDetailFindService;
import com.tpay.domains.refund.application.dto.RefundByCustomerDateResponse;
import com.tpay.domains.refund.application.dto.RefundCustomerRequest;
import com.tpay.domains.refund.application.dto.RefundFindResponseInterface;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RefundFindController {

    private final RefundDetailFindService refundDetailFindService;

    @GetMapping("/refunds/franchisee/{franchiseeIndex}")
    public ResponseEntity<List<RefundFindResponseInterface>> findList(
        @PathVariable Long franchiseeIndex,
        @RequestParam DateFilter dateFilter,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate) {

        List<RefundFindResponseInterface> responseList =
            refundDetailFindService.findList(franchiseeIndex, startDate, endDate);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/admin/refunds")
    public ResponseEntity<List<RefundFindResponseInterface>> findAll(
        @RequestParam String startDate,
        @RequestParam String endDate,
        @RequestParam RefundStatus refundStatus
    ) {
        List<RefundFindResponseInterface> response = refundDetailFindService.findAll(startDate, endDate, refundStatus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/refunds/{franchiseeIndex}")
    public ResponseEntity<List<RefundFindResponseInterface>> findAFranchisee(@PathVariable Long franchiseeIndex) {
        List<RefundFindResponseInterface> result = refundDetailFindService.findAFranchisee(franchiseeIndex);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refunds/customer/{franchiseeIndex}")
    public ResponseEntity<List<RefundByCustomerDateResponse>> findRefundsByCustomerInfo(
        @PathVariable Long franchiseeIndex,
        @RequestBody RefundCustomerRequest refundCustomerRequest
    ) {
        List<RefundByCustomerDateResponse> result = refundDetailFindService.findRefundsByCustomerInfo(franchiseeIndex, refundCustomerRequest);
        return ResponseEntity.ok(result);
    }
}
