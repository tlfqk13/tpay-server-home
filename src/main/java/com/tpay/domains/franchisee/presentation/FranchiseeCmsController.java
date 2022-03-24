package com.tpay.domains.franchisee.presentation;


import com.tpay.domains.franchisee.application.FranchiseeCmsService;
import com.tpay.domains.franchisee.application.dto.cms.FranchiseeCmsDetailResponse;
import com.tpay.domains.franchisee.application.dto.cms.FranchiseeCmsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranchiseeCmsController {

    private final FranchiseeCmsService franchiseeCmsService;

    @GetMapping("/franchisee/{franchiseeIndex}/cms")
    public ResponseEntity<FranchiseeCmsResponse> cmsReport(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) {
        FranchiseeCmsResponse result = franchiseeCmsService.cmsReport(franchiseeIndex, requestDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/franchisee/{franchiseeIndex}/cms/detail")
    public ResponseEntity<FranchiseeCmsDetailResponse> cmsDetail(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) {
        FranchiseeCmsDetailResponse result = franchiseeCmsService.cmsDetail(franchiseeIndex, requestDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/franchisee/{franchiseeIndex}/cms/downloads")
    public ResponseEntity<String> cmsDownloads(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) {
        String result = franchiseeCmsService.cmsDownloads(franchiseeIndex, requestDate);
        return ResponseEntity.ok("Asdf");
    }
}
