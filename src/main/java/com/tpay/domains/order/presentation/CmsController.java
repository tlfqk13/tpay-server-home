package com.tpay.domains.order.presentation;


import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.order.application.CmsService;
import com.tpay.domains.order.application.dto.CmsDetailResponse;
import com.tpay.domains.vat.application.dto.VatTotalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CmsController {

    private final CmsService cmsService;


    // TODO: 2022/04/27 URI 변경할 것 franchisee -> order
    // TODO: 2022/10/28 어플 마이페이지> CMS 청구내역
    @GetMapping("/franchisee/{franchiseeIndex}/cms")
    public ResponseEntity<VatTotalDto.Response> cmsReport(
            @PathVariable Long franchiseeIndex,
            @RequestParam String requestDate,
            @KtpIndexInfo IndexInfo indexInfo
            ) {
        VatTotalDto.Response result = cmsService.cmsReport(indexInfo.getIndex(), requestDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/franchisee/{franchiseeIndex}/cms/detail")
    public ResponseEntity<CmsDetailResponse> cmsDetail(
        @PathVariable Long franchiseeIndex,
        @RequestParam String requestDate,
        @KtpIndexInfo IndexInfo indexInfo
    ) {
        CmsDetailResponse result = cmsService.cmsDetail(indexInfo.getIndex(), requestDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/franchisee/{franchiseeIndex}/cms/downloads")
    public ResponseEntity<String> cmsDownloads(
            @PathVariable Long franchiseeIndex,
            @RequestParam String requestDate,
            @KtpIndexInfo IndexInfo indexInfo
    ) {
        String downloadLink = cmsService.cmsDownloads(indexInfo.getIndex(), requestDate);
        return ResponseEntity.ok(downloadLink);
    }

    // TODO: 2022/07/29 관리자가 한번에 cms 청구서 뽑는 기능
    @GetMapping("/franchisee/admin/cms/downloads")
    public ResponseEntity<String> adminCmsDownloads(
            @RequestParam String requestDate){
        cmsService.cmsAdminDownloads(requestDate);
        return ResponseEntity.ok("Asdf");
    }
}
