package com.tpay.domains.franchisee.presentation;


import com.tpay.domains.franchisee.application.FranchiseeVatService;
import com.tpay.domains.franchisee.application.dto.vat.FranchiseeVatDetailResponse;
import com.tpay.domains.franchisee.application.dto.vat.FranchiseeVatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeVatReportController {

    private final FranchiseeVatService franchiseeVatService;


    @GetMapping("/franchisee/{franchiseeIndex}/vat")
    public ResponseEntity<FranchiseeVatResponse> vatReport(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) {
        FranchiseeVatResponse result = franchiseeVatService.vatReport(franchiseeIndex, requestDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/franchisee/{franchiseeIndex}/vat/detail")
    public ResponseEntity<FranchiseeVatDetailResponse> vatDetail(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) {
        FranchiseeVatDetailResponse franchiseeVatDetailResponse = franchiseeVatService.vatDetail(franchiseeIndex, requestDate);
        return ResponseEntity.ok(franchiseeVatDetailResponse);
    }


}
