package com.tpay.domains.erp.deploy;

import com.tpay.domains.order.application.CmsService;
import com.tpay.domains.vat.application.VatDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/franchisee/admin")
public class ErpDocumentController {

    private final VatDownloadService vatDownloadService;
    private final CmsService cmsService;

    // API 호출로 관리자가 한번에 일괄 출력 기능
    @GetMapping("/vat/downloads")
    public ResponseEntity<String> vatAdminDownloads(
            @RequestParam String requestDate) {
        vatDownloadService.vatAdminDownloads(requestDate);
        return ResponseEntity.ok("ok");
    }

    // 2022/07/29 관리자가 한번에 cms 청구서 뽑는 기능
    @GetMapping("cms/downloads")
    public ResponseEntity<String> adminCmsDownloads(
            @RequestParam String requestDate){
        cmsService.cmsAdminDownloads(requestDate);
        return ResponseEntity.ok("Asdf");
    }
}
