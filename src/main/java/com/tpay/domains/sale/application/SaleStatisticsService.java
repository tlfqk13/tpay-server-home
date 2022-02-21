package com.tpay.domains.sale.application;


import com.tpay.commons.converter.StringToLocalDateConverter;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.DateSelector;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.sale.application.dto.SaleStatisticsResponseInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleStatisticsService {

  private final RefundRepository refundRepository;
  private final StringToLocalDateConverter stringToLocalDateConverter;


  public SaleStatisticsResponseInterface saleStatistics(Long franchiseeIndex, String startDate, String endDate) {
    return refundRepository.findStatistics(franchiseeIndex, startDate, endDate);
  }

  public List<SaleStatisticsResponseInterface> saleCompare(Long franchiseeIndex, String startDate, String endDate, DateSelector dateSelector) {
    LocalDate convertStartDate = stringToLocalDateConverter.convert(startDate.substring(0, 4) + "-" + startDate.substring(4) + "-01");
    LocalDate convertEndDate = stringToLocalDateConverter.convert(endDate.substring(0, 4) + "-" + endDate.substring(4) + "-01");
    List<SaleStatisticsResponseInterface> saleStatisticsResponseInterfaceList = new ArrayList<>();
    saleStatisticsResponseInterfaceList.add(refundRepository.findStatistics(franchiseeIndex, startDate, endDate));

    if(dateSelector.equals(DateSelector.MONTH)){
    String preMonthStartDate = convertStartDate.minusMonths(1).toString().replaceAll("-", "").substring(0, 6);
    String preMonthEndDate = convertEndDate.minusMonths(1).toString().replaceAll("-", "").substring(0, 6);
    saleStatisticsResponseInterfaceList.add(refundRepository.findStatistics(franchiseeIndex, preMonthStartDate, preMonthEndDate));
    }
    else if(dateSelector.equals(DateSelector.YEAR)){
    String preYearStartDate = convertStartDate.minusYears(1).toString().replaceAll("-", "").substring(0, 6);
    String preYearEndDate = convertEndDate.minusYears(1).toString().replaceAll("-", "").substring(0, 6);
    saleStatisticsResponseInterfaceList.add(refundRepository.findStatistics(franchiseeIndex, preYearStartDate, preYearEndDate));
    }
    else {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER,"DateSelector : MONTH or YEAR ");
    }

    return saleStatisticsResponseInterfaceList;
  }
}
