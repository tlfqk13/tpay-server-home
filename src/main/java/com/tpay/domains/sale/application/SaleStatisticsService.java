package com.tpay.domains.sale.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.DateSelector;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.commons.util.converter.StringToLocalDateConverter;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleStatisticsCurrentResponse;
import com.tpay.domains.sale.application.dto.SaleStatisticsPreviousResponse;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponse;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.tpay.commons.util.DateSelector.*;

@Service
@RequiredArgsConstructor
public class SaleStatisticsService {

    private final RefundRepository refundRepository;
    private final StringToLocalDateConverter stringToLocalDateConverter;

    public SaleStatisticsResponseInterface saleStatistics(Long franchiseeIndex, String targetDate, DateSelector dateSelector) {
        if (dateSelector.equals(MONTH)) {
            return refundRepository.findMonthStatistics(franchiseeIndex, targetDate);
        } else if (dateSelector.equals(YEAR)) {
            String targetDateYear = targetDate.substring(0, 4);
            return refundRepository.findYearStatistics(franchiseeIndex, targetDateYear);
        } else if (dateSelector.equals(ALL)) {
            return refundRepository.findAllStatistics(franchiseeIndex);
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "DateSelector : MONTH or YEAR ");
        }
    }

    public SaleStatisticsResponse saleCompare(Long franchiseeIndex, String targetDate, DateSelector dateSelector) {
        LocalDate convertTargetDate = stringToLocalDateConverter.convert(targetDate.substring(0, 4) + "-" + targetDate.substring(4) + "-01");
        SaleStatisticsResponseInterface curr;
        SaleStatisticsResponseInterface prev;
        if (dateSelector.equals(MONTH)) {
            String preMonthTargetDate = convertTargetDate.minusMonths(1).toString().replaceAll("-", "").substring(0, 6);
            curr = refundRepository.findMonthStatistics(franchiseeIndex, targetDate);
            prev = refundRepository.findMonthStatistics(franchiseeIndex, preMonthTargetDate);
        } else if (dateSelector.equals(YEAR)) {
            String preYearTargetDate = convertTargetDate.minusYears(1).toString().replaceAll("-", "").substring(0, 6);
            String targetDateYear = targetDate.substring(0, 4);
            String preYearTargetDateYear = preYearTargetDate.substring(0, 4);
            curr = refundRepository.findYearStatistics(franchiseeIndex, targetDateYear);
            prev = refundRepository.findYearStatistics(franchiseeIndex, preYearTargetDateYear);
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "DateSelector : MONTH or YEAR ");
        }

        SaleStatisticsCurrentResponse saleStatisticsCurrentResponse = SaleStatisticsCurrentResponse.builder()
            .totalAmount(NumberFormatConverter.addCommaToNumber(curr.getTotalAmount()) + "원")
            .totalActualAmount(NumberFormatConverter.addCommaToNumber(curr.getTotalActualAmount()) + "원")
            .totalRefund(NumberFormatConverter.addCommaToNumber(curr.getTotalRefund()) + "원")
            .totalCount(NumberFormatConverter.addCommaToNumber(curr.getTotalCount()) + "건")
            .totalCancel(NumberFormatConverter.addCommaToNumber(curr.getTotalCancel()) + "건")
            .build();

        SaleStatisticsPreviousResponse saleStatisticsPreviousResponse = SaleStatisticsPreviousResponse.builder()
            .totalAmount(NumberFormatConverter.addCommaToNumber(prev.getTotalAmount()) + "원")
            .totalActualAmount(NumberFormatConverter.addCommaToNumber(prev.getTotalActualAmount()) + "원")
            .totalRefund(NumberFormatConverter.addCommaToNumber(prev.getTotalRefund()) + "원")
            .totalCount(NumberFormatConverter.addCommaToNumber(prev.getTotalCount()) + "건")
            .totalCancel(NumberFormatConverter.addCommaToNumber(prev.getTotalCancel()) + "건")
            .build();

        return SaleStatisticsResponse.builder()
            .saleStatisticsCurrentResponse(saleStatisticsCurrentResponse)
            .saleStatisticsPreviousResponse(saleStatisticsPreviousResponse)
            .build();
    }
}
