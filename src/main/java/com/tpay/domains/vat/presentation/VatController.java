package com.tpay.domains.vat.presentation;


import com.tpay.domains.batch.vat_batch.application.VatMonthlySendService;
import com.tpay.domains.vat.application.VatDownloadService;
import com.tpay.domains.vat.application.VatHomeTaxService;
import com.tpay.domains.vat.application.VatService;
import com.tpay.domains.vat.application.dto.VatDetailResponse;
import com.tpay.domains.vat.application.dto.VatHomeTaxDto;
import com.tpay.domains.vat.application.dto.VatResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class VatController {

    private final VatService vatService;
    private final VatMonthlySendService vatMonthlySendService;

    private final VatDownloadService vatDownloadService;
    private final VatHomeTaxService homeTaxService;


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

    // TODO: 2022/07/06 환급실적명세서 다운로드 기능 시작.
    @GetMapping("/franchisee/{franchiseeIndex}/vat/downloads")
    public ResponseEntity<String> vatDownloads(
            @PathVariable Long franchiseeIndex,
            @RequestParam String requestDate
    ){
        String downloadLink = vatDownloadService.vatDownloads(franchiseeIndex,requestDate);
        return ResponseEntity.ok(downloadLink);
    }

    @GetMapping("/franchisee/{franchiseeIndex}/downloadfile")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long franchiseeIndex) throws IOException {

        ClassPathResource resource = new ClassPathResource("aaa.txt");
        File file;
        InputStream inputStream = resource.getInputStream();
        file = File.createTempFile("aaa", "txt");
        FileUtils.copyInputStreamToFile(inputStream, file);
        System.out.println("######################");
        System.out.println(file.getAbsolutePath());
        System.out.println("######################");

        System.out.println("######################");
        System.out.println(file.getName());
        System.out.println("######################");

        UrlResource urlResource = new UrlResource("file:" + file.getAbsolutePath());

        String encodedFileName = UriUtils.encode(file.getName(),StandardCharsets.UTF_8);

        // 파일 다운로드 대화상자가 뜨도록 하는 헤더를 설정해주는 것
        // Content-Disposition 헤더에 attachment; filename="업로드 파일명" 값을 준다.
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
        System.out.println("######################");
        System.out.println(contentDisposition);
        System.out.println("######################");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition)
                .body(urlResource);
    }

    // API 호출로 관리자가 한번에 일괄 출력 기능
    @GetMapping("/franchisee/{franchiseeIndex}/admin/vat/downloads")
    public ResponseEntity<String> vatMonthlyDownloads(
            @PathVariable Long franchiseeIndex,
            @RequestParam String requestMonth // ?requestMonth = 225
    ){
        vatMonthlySendService.vatMonthlyFile();
        return ResponseEntity.ok("vatMonthlySendMailFile");
    }

    @GetMapping("/franchisee/{franchiseeIndex}/vat/hometax")
    public ResponseEntity<VatHomeTaxDto.Response> homeTaxFile(@PathVariable Long franchiseeIndex,
                                              @RequestParam String requestDate) throws IOException {
        VatHomeTaxDto.Response homeTaxResponse = homeTaxService.createHomeTaxUploadFile(franchiseeIndex, requestDate);
        return ResponseEntity.ok(homeTaxResponse);
    }

}
