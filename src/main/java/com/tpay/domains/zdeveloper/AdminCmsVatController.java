package com.tpay.domains.zdeveloper;

import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.order.application.CmsService;
import com.tpay.domains.vat.application.VatDownloadService;
import com.tpay.domains.vat.application.VatHomeTaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/franchisee/admin")
public class AdminCmsVatController {

    private final VatDownloadService vatDownloadService;
    private final CmsService cmsService;
    private final VatHomeTaxService homeTaxService;

    // API 호출로 관리자가 한번에 일괄 출력 기능
    @GetMapping("/vat/downloads")
    public ResponseEntity<String> vatAdminDownloads(
            @RequestParam String requestDate,
            @RequestParam RefundType refundType) {
        vatDownloadService.vatAdminDownloads(requestDate,refundType);
        return ResponseEntity.ok("Admin VatDownloads");
    }

    @GetMapping("/vat/quarterly/downloads")
    public ResponseEntity<String> vatAdminDownloadsQuarterly(
            @RequestParam String requestDate,
            @RequestParam RefundType refundType) {
        vatDownloadService.vatAdminDownloadsQuarterly(requestDate,refundType);
        return ResponseEntity.ok("Admin vatAdminDownloads Quarterly ");
    }

    // 2022/07/29 관리자가 한번에 cms 청구서 뽑는 기능
    @GetMapping("cms/downloads")
    public ResponseEntity<String> adminCmsDownloads(
            @RequestParam String requestDate,
            @RequestParam RefundType refundType){
        cmsService.cmsAdminDownloads(requestDate,refundType);
        return ResponseEntity.ok("Admin CmsDownloads");
    }

    // 2022/12/22 관리자가 한번에 hometax 파일 생성
    @GetMapping("/hometax/downloads")
    public ResponseEntity<String> homeTaxFile(
            @RequestParam String requestDate) throws IOException {
        homeTaxService.homeTaxAdminDownloads(requestDate);
        return ResponseEntity.ok("Admin homeTaxFile");
    }

}
