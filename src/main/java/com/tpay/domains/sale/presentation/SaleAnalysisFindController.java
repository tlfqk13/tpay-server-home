package com.tpay.domains.sale.presentation;


import com.tpay.commons.util.DateFilterV2;
import com.tpay.domains.sale.application.SaleAnalysisFindService;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponseInterface;
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

    private final SaleAnalysisFindService saleAnalysisFindServiceV2;

    @GetMapping("/sales/franchiseeV2/{franchiseeIndex}")
    public ResponseEntity<List<SaleAnalysisFindResponseInterface>> findByDateRange(
        @PathVariable Long franchiseeIndex,
        @RequestParam DateFilterV2 dateFilter,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    ) {
        List<SaleAnalysisFindResponseInterface> result = saleAnalysisFindServiceV2.findByDateRange(franchiseeIndex, dateFilter, startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
