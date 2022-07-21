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

    // TODO: 2022/04/27 URI 변경할 것 franchisee -> order
    @GetMapping("/franchisee/{franchiseeIndex}/cms/downloads")
    public ResponseEntity<String> cmsDownloads(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) throws IOException {
        //String result = cmsService.cmsDownloads(franchiseeIndex, requestDate);
        for(int i=0;i<10;i++){
            String result = cmsService.cmsDownloadsTest(franchiseeIndex, i);
        }
        cmsService.zipFileDown(10);
        return ResponseEntity.ok("Asdf");
    }

    // ZIP 파일 생성 테스트용
    @GetMapping("/franchisee/{franchiseeIndex}/cms/downloads-test")
    public ResponseEntity<String> vatZipDownloadsTest(
            @PathVariable Long franchiseeIndex,
            @RequestParam String requestDate // ?requestMonth = 225
    ){
        String result = cmsService.cmsDownloadsTest(franchiseeIndex, requestDate);
        return ResponseEntity.ok("Asdf");
    }
}
