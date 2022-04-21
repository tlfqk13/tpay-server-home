package com.tpay.domains.sale.application;


import com.tpay.commons.util.DateFilter;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponse;
import com.tpay.domains.sale.application.dto.SaleAnalysisFindResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleAnalysisFindService {

    private final RefundRepository refundRepository;

    public List<SaleAnalysisFindResponse> findByDateRange(
        Long franchiseeIndex, DateFilter dateFilter, String startDate, String endDate) {


        // Custom 구현시 Custom처리해야함
        List<SaleAnalysisFindResponseInterface> saleAnalysis = refundRepository.findSaleAnalysis(franchiseeIndex, dateFilter.getStartDate(), dateFilter.getEndDate());
        return saleAnalysis.stream().map(SaleAnalysisFindResponse::new).collect(Collectors.toList());

    }
}
