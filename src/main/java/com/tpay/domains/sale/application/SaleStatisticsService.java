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

import static com.tpay.commons.util.DateSelector.*;

@Service
@RequiredArgsConstructor
public class SaleStatisticsService {

  private final RefundRepository refundRepository;
  private final StringToLocalDateConverter stringToLocalDateConverter;
  private final NumberFormatConverter numberFormatConverter;

  public SaleStatisticsResponseInterface saleStatistics(Long franchiseeIndex, String targetDate, DateSelector dateSelector) {
    if(dateSelector.equals(MONTH)){
      return refundRepository.findMonthStatistics(franchiseeIndex, targetDate);
    }
    else if(dateSelector.equals(YEAR)){
      String targetDateYear = targetDate.substring(0,4);
      return refundRepository.findYearStatistics(franchiseeIndex,targetDateYear);
    }
    else if(dateSelector.equals(ALL)){
      return refundRepository.findAllStatistics(franchiseeIndex);
    }
    else {
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
      String targetDateYear = targetDate.substring(0,4);
      String preYearTargetDateYear = preYearTargetDate.substring(0,4);
      curr = refundRepository.findYearStatistics(franchiseeIndex,targetDateYear);
      prev = refundRepository.findYearStatistics(franchiseeIndex, preYearTargetDateYear);
    } else {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "DateSelector : MONTH or YEAR ");
    }

    SaleStatisticsCurrentResponse saleStatisticsCurrentResponse = SaleStatisticsCurrentResponse.builder()
        .totalAmount(numberFormatConverter.addCommaToNumber(curr.getTotalAmount())+"원")
        .totalActualAmount(numberFormatConverter.addCommaToNumber(curr.getTotalActualAmount())+"원")
        .totalRefund(numberFormatConverter.addCommaToNumber(curr.getTotalRefund())+"원")
        .totalCount(numberFormatConverter.addCommaToNumber(curr.getTotalCount())+"건")
        .totalCancel(numberFormatConverter.addCommaToNumber(curr.getTotalCancel())+"건")
        .build();

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
