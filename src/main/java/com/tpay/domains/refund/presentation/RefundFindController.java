package com.tpay.domains.refund.presentation;

import com.tpay.domains.refund.application.RefundDetailFindService;
import com.tpay.domains.refund.application.dto.RefundByCustomerDateResponse;
import com.tpay.domains.refund.application.dto.RefundCustomerRequest;
import com.tpay.domains.refund.application.dto.RefundFindResponseInterface;
import com.tpay.domains.refund.application.dto.RefundPagingFindResponse;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.refund_test.dto.RefundDetailTotalDto;
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
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        List<RefundFindResponseInterface> responseList =
                refundDetailFindService.findList(franchiseeIndex, startDate, endDate);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/admin/refunds")
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

    @GetMapping("/admin/refunds/{franchiseeIndex}")
    public ResponseEntity<RefundDetailTotalDto.Response> findRefundDetail(
            @PathVariable Long franchiseeIndex,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        RefundDetailTotalDto.Response result = refundDetailFindService.findRefundDetail(franchiseeIndex, startDate, endDate);
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

    // TODO: 2022/08/25 중복 로그인 이슈 환급하기 눌러서 가짜 요청 날리기?
    @GetMapping("/refunds/duplicate-check")
    public ResponseEntity<String> refundDuplicateCheck(){
        return ResponseEntity.ok("ok");
    }

}
