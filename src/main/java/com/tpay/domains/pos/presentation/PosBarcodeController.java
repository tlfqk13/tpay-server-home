package com.tpay.domains.pos.presentation;


import com.tpay.domains.pos.application.PosBarcodeService;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PosBarcodeController {

    private final PosBarcodeService posBarcodeService;

    @PostMapping("/pos/refund/limit/{franchiseeIndex}")
    public ResponseEntity<Resource> barcodeMaker(
        @PathVariable Long franchiseeIndex,
        @RequestBody RefundLimitRequest refundLimitRequest) {
        return posBarcodeService.createBarCode(franchiseeIndex, refundLimitRequest);
    }
}
