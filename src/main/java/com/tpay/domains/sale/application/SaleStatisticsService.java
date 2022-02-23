package com.tpay.domains.sale.application;


import com.tpay.commons.converter.NumberFormatConverter;
import com.tpay.commons.converter.StringToLocalDateConverter;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.DateSelector;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleStatisticsCurrentResponse;
import com.tpay.domains.sale.application.dto.SaleStatisticsPreviousResponse;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponse;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SaleStatisticsService {

  private final RefundRepository refundRepository;
  private final StringToLocalDateConverter stringToLocalDateConverter;
  private final NumberFormatConverter numberFormatConverter;

  public SaleStatisticsResponseInterface saleStatistics(Long franchiseeIndex, String startDate, String endDate) {
    return refundRepository.findStatistics(franchiseeIndex, startDate, endDate);
  }

  public SaleStatisticsResponse saleCompare(Long franchiseeIndex, String startDate, String endDate, DateSelector dateSelector) {
    LocalDate convertStartDate = stringToLocalDateConverter.convert(startDate.substring(0, 4) + "-" + startDate.substring(4) + "-01");
    LocalDate convertEndDate = stringToLocalDateConverter.convert(endDate.substring(0, 4) + "-" + endDate.substring(4) + "-01");
    SaleStatisticsResponseInterface curr = refundRepository.findStatistics(franchiseeIndex, startDate, endDate);
    SaleStatisticsCurrentResponse saleStatisticsCurrentResponse = SaleStatisticsCurrentResponse.builder()
        .totalAmount(numberFormatConverter.addCommaToNumber(curr.getTotalAmount())+"원")
        .totalActualAmount(numberFormatConverter.addCommaToNumber(curr.getTotalActualAmount())+"원")
        .totalRefund(numberFormatConverter.addCommaToNumber(curr.getTotalRefund())+"원")
        .totalCount(numberFormatConverter.addCommaToNumber(curr.getTotalCount())+"건")
        .totalCancel(numberFormatConverter.addCommaToNumber(curr.getTotalCancel())+"건")
        .build();

    SaleStatisticsResponseInterface prev;
    if (dateSelector.equals(DateSelector.MONTH)) {
      String preMonthStartDate = convertStartDate.minusMonths(1).toString().replaceAll("-", "").substring(0, 6);
      String preMonthEndDate = convertEndDate.minusMonths(1).toString().replaceAll("-", "").substring(0, 6);
      prev = refundRepository.findStatistics(franchiseeIndex, preMonthStartDate, preMonthEndDate);
    } else if (dateSelector.equals(DateSelector.YEAR)) {
      String preYearStartDate = convertStartDate.minusYears(1).toString().replaceAll("-", "").substring(0, 6);
      String preYearEndDate = convertEndDate.minusYears(1).toString().replaceAll("-", "").substring(0, 6);
      prev = refundRepository.findStatistics(franchiseeIndex, preYearStartDate, preYearEndDate);
    } else {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "DateSelector : MONTH or YEAR ");
    }

    SaleStatisticsPreviousResponse saleStatisticsPreviousResponse = SaleStatisticsPreviousResponse.builder()
        .totalAmount(numberFormatConverter.addCommaToNumber(prev.getTotalAmount())+"원")
        .totalActualAmount(numberFormatConverter.addCommaToNumber(prev.getTotalActualAmount())+"원")
        .totalRefund(numberFormatConverter.addCommaToNumber(prev.getTotalRefund())+"원")
        .totalCount(numberFormatConverter.addCommaToNumber(prev.getTotalCount())+"건")
        .totalCancel(numberFormatConverter.addCommaToNumber(prev.getTotalCancel())+"건")
        .build();

    return SaleStatisticsResponse.builder()
        .saleStatisticsCurrentResponse(saleStatisticsCurrentResponse)
        .saleStatisticsPreviousResponse(saleStatisticsPreviousResponse)
        .build();
  }
}
