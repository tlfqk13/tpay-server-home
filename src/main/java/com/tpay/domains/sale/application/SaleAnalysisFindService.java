package com.tpay.domains.sale.application;


import com.tpay.commons.util.DateFilter;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleAnalysisFindService {

    private final RefundRepository refundRepository;

    public List<SaleAnalysisFindResponse> findByDateRange(
        Long franchiseeIndex, DateFilter dateFilter, LocalDate startDate, LocalDate endDate) {


        log.trace("매출분석 sttDate : {}, endDate : {}",dateFilter.getStartDate(),dateFilter.getEndDate());
        // Custom 구현시 Custom처리해야함
        List<SaleAnalysisFindResponseInterface> saleAnalysis = refundRepository.findSaleAnalysis(franchiseeIndex, dateFilter.getStartDate(), dateFilter.getEndDate());
        log.trace("매출분석 rtnDate : {}",saleAnalysis.get(0).getFormatDate());
        return saleAnalysis.stream().map(SaleAnalysisFindResponse::new).collect(Collectors.toList());

    }
}
