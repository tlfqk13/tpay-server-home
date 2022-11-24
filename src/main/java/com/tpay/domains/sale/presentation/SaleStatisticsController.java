package com.tpay.domains.sale.presentation;


import com.tpay.commons.util.DateSelector;
import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.sale.application.SaleStatisticsService;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponse;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sales/statistics")
public class SaleStatisticsController {

    private final SaleStatisticsService saleStatisticsService;

    @GetMapping("/{franchiseeIndex}")
    public ResponseEntity<SaleStatisticsResponseInterface> salesStatistics(
            @PathVariable Long franchiseeIndex,
            @RequestParam String targetDate,
            @RequestParam DateSelector dateSelector,
            @KtpIndexInfo IndexInfo indexInfo
            ) {
        SaleStatisticsResponseInterface result = saleStatisticsService.saleStatistics(indexInfo.getIndex(), targetDate, dateSelector);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail/{franchiseeIndex}")
    public ResponseEntity<SaleStatisticsResponse> saleCompare(
        @PathVariable Long franchiseeIndex,
        @RequestParam String targetDate,
        @RequestParam DateSelector dateSelector,
        @KtpIndexInfo IndexInfo indexInfo
    ) {
        SaleStatisticsResponse result = saleStatisticsService.saleCompare(indexInfo.getIndex(), targetDate, dateSelector);
        return ResponseEntity.ok(result);
    }

}
