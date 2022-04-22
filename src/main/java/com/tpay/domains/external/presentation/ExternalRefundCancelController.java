package com.tpay.domains.external.presentation;


import com.tpay.domains.external.application.ExternalRefundCancelService;
import com.tpay.domains.external.application.dto.ExternalRefundCancelRequest;
import com.tpay.domains.external.application.dto.ExternalRefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalRefundCancelController {

    private final ExternalRefundCancelService externalRefundCancelService;

    @PostMapping("/external/refund/cancel")
    public ResponseEntity<ExternalRefundResponse> externalRefundCancel(@RequestBody ExternalRefundCancelRequest externalRefundCancelRequest) {

        ExternalRefundResponse result = externalRefundCancelService.cancel(externalRefundCancelRequest);
        return ResponseEntity.ok(result);

    }
}
