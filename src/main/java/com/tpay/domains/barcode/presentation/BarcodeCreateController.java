package com.tpay.domains.barcode.presentation;


import com.tpay.domains.barcode.application.BarcodeCreateService;
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
public class BarcodeCreateController {

    private final BarcodeCreateService barcodeCreateService;

    @PostMapping("/pos/refund/limit/{franchiseeIndex}")
    public ResponseEntity<Resource> barcodeMaker(
        @PathVariable Long franchiseeIndex,
        @RequestBody RefundLimitRequest refundLimitRequest) {
        return barcodeCreateService.createBarCode(franchiseeIndex, refundLimitRequest);
    }
}