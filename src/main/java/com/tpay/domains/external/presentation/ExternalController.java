package com.tpay.domains.external.presentation;

import com.tpay.domains.external.application.ExternalService;
import com.tpay.domains.external.application.dto.ExternalRefundResponse;
import com.tpay.domains.external.application.dto.ExternalStatusRequestResponse;
import com.tpay.domains.external.application.dto.ExternalStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ExternalController {

    private final ExternalService externalService;


    // 바코드 스캔 이후 자동으로 화면 넘어갈 때 사용되는 주기적인 요청
    // 현재 미사용
    @GetMapping("/external/refund/result/{externalRefundIndex}")
    public ResponseEntity<ExternalStatusRequestResponse> statusRequest(@PathVariable Long externalRefundIndex) {
        return externalService.statusRequest(externalRefundIndex);
    }

    @PatchMapping("/external/refund/confirm")
    public ResponseEntity<ExternalRefundResponse> statusUpdate(
        @RequestBody ExternalStatusUpdateDto externalStatusUpdateDto) {
        ExternalRefundResponse externalRefundResponse = externalService.statusUpdate(externalStatusUpdateDto);
        return ResponseEntity.ok(externalRefundResponse);
    }
}
