package com.tpay.domains.vat.presentation;


import com.tpay.domains.vat.application.VatService;
import com.tpay.domains.vat.application.dto.VatDetailResponse;
import com.tpay.domains.vat.application.dto.VatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VatReportController {

    private final VatService vatService;


    @GetMapping("/franchisee/{franchiseeIndex}/vat")
    public ResponseEntity<VatResponse> vatReport(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) {
        VatResponse result = vatService.vatReport(franchiseeIndex, requestDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/franchisee/{franchiseeIndex}/vat/detail")
    public ResponseEntity<VatDetailResponse> vatDetail(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) {
        VatDetailResponse vatDetailResponse = vatService.vatDetail(franchiseeIndex, requestDate);
        return ResponseEntity.ok(vatDetailResponse);
    }


}
