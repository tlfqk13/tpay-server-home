package com.tpay.domains.erp.deploy;

import com.tpay.domains.refund.application.RefundDetailFindService;
import com.tpay.domains.refund.application.dto.RefundDetailTotalDto;
import com.tpay.domains.refund.application.dto.RefundPagingFindResponse;
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
            @RequestParam RefundStatus refundStatus,
            @RequestParam String searchKeyword
    ) {
        RefundPagingFindResponse response = refundDetailFindService.findAll(page,refundStatus,startDate, endDate,searchKeyword);
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
}
