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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
    ) {
        String result = vatDownloadService.vatDownloads(franchiseeIndex, requestDate);
        return ResponseEntity.ok("Asdf");
    }

    @GetMapping("/franchisee/{franchiseeIndex}/vat/test")
    //public ResponseEntity<Resource> downloadFile(
    public void downloadFile(
            //HttpServletRequest request,
            @RequestHeader(name = HttpHeaders.USER_AGENT) String userAgent,
            HttpServletResponse response,
            @PathVariable Long franchiseeIndex,
            @RequestParam String requestDate
    ) {
        XSSFWorkbook downloadExcelFile = vatDownloadService.vatDownloadsTest(franchiseeIndex,"221");
        List<String> name = vatService.findPersonalInfo(franchiseeIndex,requestDate);
        String fileName = "cms";
        System.out.println("########################");
        System.out.println(userAgent);
        System.out.println("########################");
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\""); // 한글이면 깨짐 지금
        try {
            downloadExcelFile.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // API 호출로 관리자가 한번에 일괄 출력 기능
    @GetMapping("/franchisee/{franchiseeIndex}/vat/monthly-downloads")
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
