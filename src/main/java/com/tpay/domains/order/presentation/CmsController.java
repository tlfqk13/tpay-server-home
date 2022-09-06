package com.tpay.domains.order.presentation;


import com.tpay.domains.order.application.CmsService;
import com.tpay.domains.order.application.dto.CmsDetailResponse;
import com.tpay.domains.order.application.dto.CmsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CmsController {

    private final CmsService cmsService;


    // TODO: 2022/04/27 URI 변경할 것 franchisee -> order
    @GetMapping("/franchisee/{franchiseeIndex}/cms")
    public ResponseEntity<CmsResponse> cmsReport(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) {
        CmsResponse result = cmsService.cmsReport(franchiseeIndex, requestDate);
        return ResponseEntity.ok(result);
    }

    // TODO: 2022/04/27 URI 변경할 것 franchisee -> order
    @GetMapping("/franchisee/{franchiseeIndex}/cms/detail")
    public ResponseEntity<CmsDetailResponse> cmsDetail(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) {
        CmsDetailResponse result = cmsService.cmsDetail(franchiseeIndex, requestDate);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/franchisee/{franchiseeIndex}/cms/downloads")
    public ResponseEntity<String> cmsDownloads(
            @PathVariable Long franchiseeIndex,
            @RequestParam String requestDate
    ) {
        cmsService.cmsDownloads(franchiseeIndex, requestDate);
        return ResponseEntity.ok("Asdf");
    }

    // TODO: 2022/07/29 관리자가 한번에 cms 청구서 뽑으려고
    @GetMapping("/franchisee/{franchiseeIndex}/cms-admin/downloads")
    public ResponseEntity<String> adminCmsDownloads(
            @PathVariable Long franchiseeIndex
    ) {
        cmsService.cmsAdminDownloads();
        return ResponseEntity.ok("Asdf");
    }
}
