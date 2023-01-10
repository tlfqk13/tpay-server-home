package com.tpay.domains.erp.deploy;

import com.tpay.domains.customer.application.dto.DepartureStatus;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.refund.application.RefundDetailFindService;
import com.tpay.domains.refund.application.dto.RefundDetailTotalDto;
import com.tpay.domains.refund.application.dto.RefundPagingFindResponse;
import com.tpay.domains.refund.application.dto.RefundPaymentDto;
import com.tpay.domains.refund.domain.PaymentStatus;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/refunds")
public class ErpRefundDetailController {
    private final RefundDetailFindService refundDetailFindService;

    @GetMapping
    public ResponseEntity<RefundPagingFindResponse> findAll(
            @RequestParam int page,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String searchKeyword,
            @RequestParam RefundType refundType,
            @RequestParam RefundStatus refundStatus,
            @RequestParam DepartureStatus departureStatus,
            @RequestParam PaymentStatus paymentStatus
    ) {
        RefundPagingFindResponse response = refundDetailFindService.findAll(page, startDate, endDate, searchKeyword
                , refundType, refundStatus, departureStatus, paymentStatus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{franchiseeIndex}")
    public ResponseEntity<RefundDetailTotalDto.Response> findRefundDetail(
            @PathVariable Long franchiseeIndex,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        RefundDetailTotalDto.Response result = refundDetailFindService.findRefundDetail(franchiseeIndex, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail/{refundIndex}")
    public ResponseEntity<RefundPaymentDto.Response> findPaymentDetail(
            @PathVariable Long refundIndex
    ) {
        RefundPaymentDto.Response result = refundDetailFindService.findPaymentDetail(refundIndex);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/detail/{refundIndex}")
    public ResponseEntity<String> registerPaymentDetail(
            @PathVariable Long refundIndex,
            @RequestBody RefundPaymentDto.Request request
    ) {
        refundDetailFindService.registerPaymentDetail(refundIndex, request);
        return ResponseEntity.ok("registerPaymentDetail");
    }

    @PatchMapping("/detail/{refundIndex}")
    public ResponseEntity<String> updatePaymentDetail(
            @PathVariable Long refundIndex,
            @RequestBody RefundPaymentDto.Request request
    ) {
        refundDetailFindService.updatePaymentDetail(refundIndex, request);
        return ResponseEntity.ok("updatePaymentDetail");
    }
}
