package com.tpay.domains.sale.presentation;


import com.tpay.commons.util.DateSelector;
import com.tpay.commons.util.IndexInfo;
import com.tpay.commons.util.resolver.KtpIndexInfo;
import com.tpay.domains.sale.application.SaleStatisticsService;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponse;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sales/statistics")
public class SaleStatisticsController {

    private final SaleStatisticsService saleStatisticsService;

    @GetMapping
    public ResponseEntity<SaleStatisticsResponseInterface> salesStatistics(
            @RequestParam String targetDate,
            @RequestParam DateSelector dateSelector,
            @KtpIndexInfo IndexInfo indexInfo
    ) {
        SaleStatisticsResponseInterface result = saleStatisticsService.saleStatistics(indexInfo.getIndex(), targetDate, dateSelector);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail")
    public ResponseEntity<SaleStatisticsResponse> saleCompare(
            @RequestParam String targetDate,
            @RequestParam DateSelector dateSelector,
            @KtpIndexInfo IndexInfo indexInfo
    ) {
        SaleStatisticsResponse result = saleStatisticsService.saleCompare(indexInfo.getIndex(), targetDate, dateSelector);
        return ResponseEntity.ok(result);
    }

}
