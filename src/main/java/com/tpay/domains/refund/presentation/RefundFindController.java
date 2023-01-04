package com.tpay.domains.refund.presentation;

import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.refund.application.RefundDetailFindService;
import com.tpay.domains.refund.application.dto.RefundByCustomerDateResponse;
import com.tpay.domains.refund.application.dto.RefundCustomerRequest;
import com.tpay.domains.refund.application.dto.RefundFindResponseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class RefundFindController {
    private final RefundDetailFindService refundDetailFindService;

    @GetMapping("/refunds/franchisee")
    public ResponseEntity<List<RefundFindResponseInterface>> findList(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @KtpIndexInfo IndexInfo indexInfo) {

        List<RefundFindResponseInterface> responseList =
                refundDetailFindService.findList(indexInfo.getIndex(), startDate, endDate);
        return ResponseEntity.ok(responseList);
    }

    // 환급 취소 > 환급 리스트
    @PostMapping("/refunds/customer")
    public ResponseEntity<List<RefundByCustomerDateResponse>> findRefundsByCustomerInfo(
            @RequestBody RefundCustomerRequest refundCustomerRequest,
            @KtpIndexInfo IndexInfo indexInfo
    ) {
        List<RefundByCustomerDateResponse> result
                = refundDetailFindService.findRefundsByCustomerInfo(indexInfo.getIndex(), refundCustomerRequest);
        return ResponseEntity.ok(result);
    }

    // TODO: 2022/08/25 중복 로그인 이슈 환급하기 눌러서 가짜 요청 날리기?
    @GetMapping("/refunds/duplicate-check")
    public ResponseEntity<String> refundDuplicateCheck() {
        return ResponseEntity.ok("ok");
    }

}
