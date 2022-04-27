package com.tpay.domains.order.application;

import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.vat.application.dto.VatDetailResponseInterface;
import com.tpay.domains.vat.application.dto.VatReportResponseInterface;
import com.tpay.domains.vat.application.dto.VatResponse;
import com.tpay.domains.vat.application.dto.VatTotalResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public VatResponse findQuarterlyVatReport(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {

        VatReportResponseInterface queryResult = orderRepository.findQuarterlyVatReport(franchiseeIndex, startDate, endDate);
        if (queryResult == null) {
            return new VatResponse(true);
        }

        return VatResponse.builder()
            .totalAmount(queryResult.getTotalAmount())
            .totalCount(queryResult.getTotalCount())
            .totalVat(queryResult.getTotalVat())
            .totalSupply(queryResult.getTotalSupply())
            .build();

    }

    public List<String> findQuarterlyTotal(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        VatTotalResponseInterface vatTotalResponseInterface = orderRepository.findQuarterlyTotal(franchiseeIndex, startDate, endDate);
        List<String> result = new ArrayList<>();
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalCount()));
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalAmount()));
        result.add(NumberFormatConverter.addCommaToNumber(vatTotalResponseInterface.getTotalVat()));
        return result;
    }

    public List<List<String>> findQuarterlyDetail(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        List<VatDetailResponseInterface> vatDetailResponseInterfaceList = orderRepository.findQuarterlyVatDetail(franchiseeIndex, startDate, endDate);
        List<List<String>> detailResult = new ArrayList<>();
        for (VatDetailResponseInterface vatDetailResponseInterface : vatDetailResponseInterfaceList) {
            List<String> baseList = new ArrayList<>();
            baseList.add(vatDetailResponseInterface.getPurchaseSerialNumber());
            baseList.add(vatDetailResponseInterface.getSaleDate());
            baseList.add(vatDetailResponseInterface.getTakeoutConfirmNumber());
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getRefundAmount()));
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getAmount()));
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseInterface.getVat()));
            detailResult.add(baseList);
        }
        return detailResult;
    }
}
