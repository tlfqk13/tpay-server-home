package com.tpay.domains.external.presentation;

import com.tpay.domains.external.application.ExternalService;
import com.tpay.domains.external.application.dto.ExternalStatusRequestResponse;
import com.tpay.domains.external.application.dto.ExternalStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ExternalController {

    private final ExternalService externalService;


    @GetMapping("/external/refund/result/{externalRefundIndex}")
    public ResponseEntity<ExternalStatusRequestResponse> statusRequest(@PathVariable Long externalRefundIndex) {
        return externalService.statusRequest(externalRefundIndex);
    }

    @PatchMapping("/external/refund/confirm/{externalRefundIndex}")
    public void statusUpdate(
        @PathVariable Long externalRefundIndex,
        @RequestBody ExternalStatusUpdateDto externalStatusUpdateDto) {
        externalService.statusUpdate(externalRefundIndex, externalStatusUpdateDto);
    }
}
