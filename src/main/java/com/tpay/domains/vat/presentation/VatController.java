package com.tpay.domains.vat.presentation;

import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.vat.application.VatDownloadService;
import com.tpay.domains.vat.application.VatHomeTaxService;
import com.tpay.domains.vat.application.VatService;
import com.tpay.domains.vat.application.dto.VatDetailResponse;
import com.tpay.domains.vat.application.dto.VatHomeTaxDto;
import com.tpay.domains.vat.application.dto.VatTotalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/franchisee")
public class VatController {

    private final VatService vatService;
    private final VatDownloadService vatDownloadService;
    private final VatHomeTaxService homeTaxService;

    @GetMapping("/vat")
    public ResponseEntity<VatTotalDto.Response> vatReport(
            @RequestParam String requestDate,
            @KtpIndexInfo IndexInfo indexInfo
    ) {
        VatTotalDto.Response result = vatService.vatReport(indexInfo.getIndex(), requestDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/vat/detail")
    public ResponseEntity<VatDetailResponse> vatDetail(
            @RequestParam String requestDate,
            @KtpIndexInfo IndexInfo indexInfo
    ) {
        VatDetailResponse vatDetailResponse = vatService.vatDetail(indexInfo.getIndex(), requestDate);
        return ResponseEntity.ok(vatDetailResponse);
    }

    // 2022/07/06 환급실적명세서 다운로드 기능 시작점.
    @GetMapping("/vat/downloads")
    public ResponseEntity<String> vatDownloads(
            @RequestParam String requestDate,
            @KtpIndexInfo IndexInfo indexInfo
    ) {
        String downloadLink = vatDownloadService.vatDownloads(indexInfo.getIndex(), requestDate);
        return ResponseEntity.ok(downloadLink);
    }

    @GetMapping("/vat/hometax")
    public ResponseEntity<VatHomeTaxDto.Response> homeTaxFile(
            @RequestParam String requestDate,
            @KtpIndexInfo IndexInfo indexInfo) throws IOException {
        VatHomeTaxDto.Response homeTaxResponse = homeTaxService.createHomeTaxUploadFile(indexInfo.getIndex(), requestDate);
        return ResponseEntity.ok(homeTaxResponse);
    }
}
