package com.tpay.domains.sale.application;


import com.tpay.commons.util.DateFilter;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleAnalysisFindService {

    private final RefundRepository refundRepository;
    // TODO: 2022/08/01 가맹점 현황 > 환급 상세 > 환급 내역 총계
    public List<SaleAnalysisFindResponse> findByDateRange(
        Long franchiseeIndex, DateFilter dateFilter, LocalDate startDate, LocalDate endDate) {

        try {
            List<SaleAnalysisFindResponseInterface> saleAnalysis = refundRepository.findSaleAnalysis(franchiseeIndex, dateFilter.getStartDate(), dateFilter.getEndDate());
            return saleAnalysis.stream().map(SaleAnalysisFindResponse::new).collect(Collectors.toList());
        } catch (IndexOutOfBoundsException e) {
            return new ArrayList<>();
        }

    }
}
