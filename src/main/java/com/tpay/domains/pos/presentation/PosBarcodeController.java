package com.tpay.domains.pos.presentation;


import com.tpay.domains.pos.application.PosBarcodeService;
import com.tpay.domains.pos.application.dto.PosBarcodeResponse;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<PosBarcodeResponse> save(
        @PathVariable Long franchiseeIndex,
        @RequestBody RefundLimitRequest refundLimitRequest) {
        PosBarcodeResponse posBarcodeResponse = posBarcodeService.saveAndCreateBarcode(franchiseeIndex, refundLimitRequest);
        return ResponseEntity.ok(posBarcodeResponse);
    }
}
