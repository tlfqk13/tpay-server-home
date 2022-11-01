package com.tpay.domains.vat.presentation;


import com.tpay.domains.vat.application.VatDownloadService;
import com.tpay.domains.vat.application.VatHomeTaxService;
import com.tpay.domains.vat.application.VatService;
import com.tpay.domains.vat.application.dto.VatDetailResponse;
import com.tpay.domains.vat.application.dto.VatHomeTaxDto;
import com.tpay.domains.vat.application.dto.VatTotalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class VatController {

    private final VatService vatService;
    private final VatDownloadService vatDownloadService;
    private final VatHomeTaxService homeTaxService;


    @GetMapping("/franchisee/{franchiseeIndex}/vat")
    public ResponseEntity<VatTotalDto.Response> vatReport(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate
    ) {
        VatTotalDto.Response result = vatService.vatReport(franchiseeIndex, requestDate);
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

    // TODO: 2022/07/06 환급실적명세서 다운로드 기능 시작.
    @GetMapping("/franchisee/{franchiseeIndex}/vat/downloads")
    public ResponseEntity<String> vatDownloads(
            @PathVariable Long franchiseeIndex,
            @RequestParam String requestDate
    ){
        String downloadLink = vatDownloadService.vatDownloads(franchiseeIndex,requestDate);
        return ResponseEntity.ok(downloadLink);
    }
    // API 호출로 관리자가 한번에 일괄 출력 기능
    @GetMapping("/franchisee/admin/vat/downloads")
    public ResponseEntity<String> vatAdminDownloads()
    {
        vatDownloadService.vatAdminDownloads();
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/franchisee/{franchiseeIndex}/vat/hometax")
    public ResponseEntity<VatHomeTaxDto.Response> homeTaxFile(@PathVariable Long franchiseeIndex,
                                              @RequestParam String requestDate) throws IOException {
        VatHomeTaxDto.Response homeTaxResponse = homeTaxService.createHomeTaxUploadFile(franchiseeIndex, requestDate);
        return ResponseEntity.ok(homeTaxResponse);
    }
}
