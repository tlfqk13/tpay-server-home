package com.tpay.domains.order.presentation;


import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.order.application.CmsService;
import com.tpay.domains.order.application.dto.CmsDetailResponse;
import com.tpay.domains.vat.application.dto.VatTotalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/franchisee")
public class CmsController {

    private final CmsService cmsService;


    // TODO: 2022/04/27 URI 변경할 것 franchisee -> order
    // 2022/10/28 어플 마이페이지> CMS 청구내역
    @GetMapping("/cms")
    public ResponseEntity<VatTotalDto.Response> cmsReport(
            @RequestParam String requestDate,
            @KtpIndexInfo IndexInfo indexInfo
    ) {
        VatTotalDto.Response result = cmsService.cmsReport(indexInfo.getIndex(), requestDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cms/detail")
    public ResponseEntity<CmsDetailResponse> cmsDetail(
            @RequestParam String requestDate,
            @KtpIndexInfo IndexInfo indexInfo
    ) {
        CmsDetailResponse result = cmsService.cmsDetail(indexInfo.getIndex(), requestDate);
        return ResponseEntity.ok(result);
    }
}
