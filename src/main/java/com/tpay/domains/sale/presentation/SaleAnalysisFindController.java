package com.tpay.domains.sale.presentation;


import com.tpay.commons.util.DateFilter;
import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.sale.application.SaleAnalysisFindService;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SaleAnalysisFindController {

    private final SaleAnalysisFindService saleAnalysisFindService;

    @GetMapping("/sales/franchisee/{franchiseeIndex}")
    public ResponseEntity<List<SaleAnalysisFindResponse>> findByDateRange(
            @PathVariable Long franchiseeIndex,
            @RequestParam DateFilter dateFilter,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @KtpIndexInfo IndexInfo indexInfo
            ) {
        List<SaleAnalysisFindResponse> result = saleAnalysisFindService.findByDateRange(indexInfo.getIndex(), dateFilter, startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
