package com.tpay.domains.sale.presentation;


import com.tpay.commons.util.DateFilter;
import com.tpay.domains.sale.application.SaleAnalysisFindService;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SaleAnalysisFindController {

    private final SaleAnalysisFindService saleAnalysisFindService;

    // TODO: 2022/04/21 URI 변경
    @GetMapping("/sales/franchiseeV2/{franchiseeIndex}")
    public ResponseEntity<List<SaleAnalysisFindResponse>> findByDateRange(
        @PathVariable Long franchiseeIndex,
        @RequestParam DateFilter dateFilter,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    ) {
        List<SaleAnalysisFindResponse> result = saleAnalysisFindService.findByDateRange(franchiseeIndex, dateFilter, startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
