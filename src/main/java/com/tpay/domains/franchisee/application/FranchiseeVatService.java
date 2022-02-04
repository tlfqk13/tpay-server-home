package com.tpay.domains.franchisee.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.dto.vat.*;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import com.tpay.domains.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FranchiseeVatService {

  private final OrderRepository orderRepository;
  private final FranchiseeFindService franchiseeFindService;
  private final FranchiseeUploadFindService franchiseeUploadFindService;

  public FranchiseeVatReportResponseInterface vatReport(Long franchiseeIndex, String requestDate) {
    List<Object> localDates = setUpDate(requestDate);
    LocalDate startDate = (LocalDate) localDates.get(0);
    LocalDate endDate = (LocalDate) localDates.get(1);
    return orderRepository.findQuarterlyVatReport(franchiseeIndex, startDate, endDate);
  }

  public FranchiseeVatDetailResponse vatDetail(Long franchiseeIndex, String requestDate) {
    List<Object> localDates = setUpDate(requestDate);
    LocalDate startDate =(LocalDate) localDates.get(0);
    LocalDate endDate =(LocalDate) localDates.get(1);
    String saleTerm = (String) localDates.get(2);
    //연월일
    //1. 제출자 인적사항
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
    FranchiseeUploadEntity franchiseeUploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeIndex);
    VatDetailResponsePersonalInfo vatDetailResponsePersonalInfo = VatDetailResponsePersonalInfo.builder()
        .sellerName(franchiseeEntity.getSellerName())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .storeName(franchiseeEntity.getStoreName())
        .storeAddress(franchiseeEntity.getStoreAddressBasic()+" "+franchiseeEntity.getStoreAddressDetail())
        .saleTerm(saleTerm)
        .taxFreeStoreNumber(franchiseeUploadEntity.getTaxFreeStoreNumber())
        .build();

    //2. 물품판매 총합계
    FranchiseeVatTotalResponseInterface franchiseeVatTotalResponseInterface = orderRepository.findQuarterlyTotal(franchiseeIndex, startDate, endDate);
    VatDetailResponseTotalInfo vatDetailResponseTotalInfo = VatDetailResponseTotalInfo.builder()
        .totalCount(franchiseeVatTotalResponseInterface.getTotalCount())
        .totalAmount(franchiseeVatTotalResponseInterface.getTotalAmount())
        .totalVat(franchiseeVatTotalResponseInterface.getTotalVat())
        .build();

    //3. 물품판매 명세
    List<FranchiseeVatDetailResponseInterface> franchiseeVatDetailResponseInterfaceList = orderRepository.findQuarterlyVatDetail(franchiseeIndex, startDate, endDate);
    List<VatDetailResponseDetailInfo> vatDetailResponseDetailInfoList = franchiseeVatDetailResponseInterfaceList.stream()
        .map(franchiseeVatDetailResponseInterface ->
            VatDetailResponseDetailInfo.builder()
                .purchaseSerialNumber(franchiseeVatDetailResponseInterface.getPurchaseSerialNumber())
                .saleDate(franchiseeVatDetailResponseInterface.getSaleDate())
                .takeoutConfirmNumber(franchiseeVatDetailResponseInterface.getTakeoutConfirmNumber())
                .amount(franchiseeVatDetailResponseInterface.getAmount())
                .vat(franchiseeVatDetailResponseInterface.getVat())
                .build())
        .collect(Collectors.toList());

    return FranchiseeVatDetailResponse.builder()
        .vatDetailResponsePersonalInfo(vatDetailResponsePersonalInfo)
        .vatDetailResponseTotalInfo(vatDetailResponseTotalInfo)
        .vatDetailResponseDetailInfoList(vatDetailResponseDetailInfoList)
        .build();
  }

  public List<Object> setUpDate(String requestDatePart){
    List<Object> dateList = new ArrayList<>();
    String requestDate = "20"+requestDatePart;
    String year = requestDate.substring(0,4);
    String halfOfYear = requestDate.substring(4);
    LocalDate startDate;
    LocalDate endDate;
    String saleTerm;
    if (!(requestDate.length() == 5 && (halfOfYear.equals("1") || halfOfYear.equals("2")))){
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER,"Invalid request data format");
    }

    if(halfOfYear.equals("1")){
      startDate = LocalDate.parse(year+"-01-01");
      endDate = LocalDate.parse(year+"-06-30");
      saleTerm = year+"년01월01일~"+year+"년06월30일";
    }
    else{
      startDate = LocalDate.parse(year+"-07-01");
      endDate = LocalDate.parse(year+"-12-31");
      saleTerm = year+"년07월01일~"+year+"년12월31일";
    }
    dateList.add(startDate);
    dateList.add(endDate);
    dateList.add(saleTerm);
    return dateList;
  }
}
