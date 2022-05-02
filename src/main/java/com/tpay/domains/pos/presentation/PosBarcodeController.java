package com.tpay.domains.pos.presentation;


import com.tpay.domains.pos.application.PosBarcodeService;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundLimitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PosBarcodeController {

    private final PosBarcodeService posBarcodeService;

    @PostMapping("/pos/refund/limit/{franchiseeIndex}")
    public ResponseEntity<RefundLimitResponse> save(
        @PathVariable Long franchiseeIndex,
        @RequestBody RefundLimitRequest refundLimitRequest) {
        RefundLimitResponse result = posBarcodeService.save(franchiseeIndex, refundLimitRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pos/barcode/{externalRefundIndex}")
    public ResponseEntity<Resource> barcodeMaker(@PathVariable Long externalRefundIndex) {
        return posBarcodeService.createBarcode(externalRefundIndex);
    }

}
