package com.tpay.domains.franchisee.application;


import com.tpay.commons.util.converter.NumberFormatConverter;
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

    public FranchiseeVatResponse vatReport(Long franchiseeIndex, String requestDate) {
        List<Object> localDates = setUpDate(requestDate);
        LocalDate startDate = (LocalDate) localDates.get(0);
        LocalDate endDate = (LocalDate) localDates.get(1);
        FranchiseeVatReportResponseInterface queryResult = orderRepository.findQuarterlyVatReport(franchiseeIndex, startDate, endDate);
        if (queryResult == null) {
            return FranchiseeVatResponse.builder().totalAmount("0").totalCount("0").totalVat("0").totalSupply("0").build();
        }
        return FranchiseeVatResponse.builder()
            .totalAmount(queryResult.getTotalAmount())
            .totalCount(queryResult.getTotalCount())
            .totalVat(queryResult.getTotalVat())
            .totalSupply(queryResult.getTotalSupply())
            .build();
    }

    public FranchiseeVatDetailResponse vatDetail(Long franchiseeIndex, String requestDate) {
        List<Object> localDates = setUpDate(requestDate);
        LocalDate startDate = (LocalDate) localDates.get(0);
        LocalDate endDate = (LocalDate) localDates.get(1);
        String saleTerm = (String) localDates.get(2);
        //연월일
        //1. 제출자 인적사항
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        FranchiseeUploadEntity franchiseeUploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeIndex);
        VatDetailResponsePersonalInfo vatDetailResponsePersonalInfo = VatDetailResponsePersonalInfo.builder()
            .sellerName(franchiseeEntity.getSellerName())
            .businessNumber(franchiseeEntity.getBusinessNumber())
            .storeName(franchiseeEntity.getStoreName())
            .storeAddress(franchiseeEntity.getStoreAddressBasic() + " " + franchiseeEntity.getStoreAddressDetail())
            .saleTerm(saleTerm)
            .taxFreeStoreNumber(franchiseeUploadEntity.getTaxFreeStoreNumber())
            .build();
        //1-1. 제출자 인적사항 리스트변환 (추가 요청에 의함)
        List<Object> vatDetailResponsePersonalInfoList = new ArrayList<>();
        vatDetailResponsePersonalInfoList.add(vatDetailResponsePersonalInfo.getSellerName());
        String businessNumberBar = NumberFormatConverter.addBarToBusinessNumber(vatDetailResponsePersonalInfo.getBusinessNumber());
        vatDetailResponsePersonalInfoList.add(businessNumberBar);
        vatDetailResponsePersonalInfoList.add(vatDetailResponsePersonalInfo.getStoreName());
        vatDetailResponsePersonalInfoList.add(vatDetailResponsePersonalInfo.getStoreAddress());
        vatDetailResponsePersonalInfoList.add(vatDetailResponsePersonalInfo.getSaleTerm());
        vatDetailResponsePersonalInfoList.add(vatDetailResponsePersonalInfo.getTaxFreeStoreNumber());


        //2. 물품판매 총합계
        FranchiseeVatTotalResponseInterface franchiseeVatTotalResponseInterface = orderRepository.findQuarterlyTotal(franchiseeIndex, startDate, endDate);
        VatDetailResponseTotalInfo vatDetailResponseTotalInfo = VatDetailResponseTotalInfo.builder()
            .totalCount(franchiseeVatTotalResponseInterface.getTotalCount())
            .totalAmount(franchiseeVatTotalResponseInterface.getTotalAmount())
            .totalVat(franchiseeVatTotalResponseInterface.getTotalVat())
            .build();

        //2-1 리스트 변환
        List<Object> vatDetailResponseTotalInfoList = new ArrayList<>();
        vatDetailResponseTotalInfoList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseTotalInfo.getTotalCount()));
        vatDetailResponseTotalInfoList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseTotalInfo.getTotalAmount()));
        vatDetailResponseTotalInfoList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseTotalInfo.getTotalVat()));


        //3. 물품판매 명세
        List<FranchiseeVatDetailResponseInterface> franchiseeVatDetailResponseInterfaceList = orderRepository.findQuarterlyVatDetail(franchiseeIndex, startDate, endDate);
        List<VatDetailResponseDetailInfo> vatDetailResponseDetailInfoList = franchiseeVatDetailResponseInterfaceList.stream()
            .map(franchiseeVatDetailResponseInterface ->
                VatDetailResponseDetailInfo.builder()
                    .purchaseSerialNumber(franchiseeVatDetailResponseInterface.getPurchaseSerialNumber())
                    .saleDate(franchiseeVatDetailResponseInterface.getSaleDate())
                    .takeoutConfirmNumber(franchiseeVatDetailResponseInterface.getTakeoutConfirmNumber())
                    .refundAmount(franchiseeVatDetailResponseInterface.getRefundAmount())
                    .amount(franchiseeVatDetailResponseInterface.getAmount())
                    .vat(franchiseeVatDetailResponseInterface.getVat())
                    .build())
            .collect(Collectors.toList());

        //3-1 리스트 변환 (RefundAmount 추가됨)
        List<List<Object>> detailList = new ArrayList<>();
        for (VatDetailResponseDetailInfo vatDetailResponseDetailInfo : vatDetailResponseDetailInfoList) {
            List<Object> baseList = new ArrayList<>();
            baseList.add(vatDetailResponseDetailInfo.getPurchaseSerialNumber());
            baseList.add(vatDetailResponseDetailInfo.getSaleDate());
            baseList.add(vatDetailResponseDetailInfo.getTakeoutConfirmNumber());
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseDetailInfo.getRefundAmount()));
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseDetailInfo.getAmount()));
            baseList.add(NumberFormatConverter.addCommaToNumber(vatDetailResponseDetailInfo.getVat()));
            detailList.add(baseList);
        }

        return FranchiseeVatDetailResponse.builder()
            .vatDetailResponsePersonalInfoList(vatDetailResponsePersonalInfoList)
            .vatDetailResponseTotalInfoList(vatDetailResponseTotalInfoList)
            .vatDetailResponseDetailInfoListList(detailList)
            .build();
    }

    public List<Object> setUpDate(String requestDatePart) {
        List<Object> dateList = new ArrayList<>();
        String requestDate = "20" + requestDatePart;
        String year = requestDate.substring(0, 4);
        String halfOfYear = requestDate.substring(4);
        LocalDate startDate;
        LocalDate endDate;
        String saleTerm;
        if (!(requestDate.length() == 5 && (halfOfYear.equals("1") || halfOfYear.equals("2")))) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid request data format");
        }

        if (halfOfYear.equals("1")) {
            startDate = LocalDate.parse(year + "-01-01");
            endDate = LocalDate.parse(year + "-06-30");
            saleTerm = year + "년 01월 01일 ~ " + year + "년 06월 30일";
        } else {
            startDate = LocalDate.parse(year + "-07-01");
            endDate = LocalDate.parse(year + "-12-31");
            saleTerm = year + "년 07월 01일 ~ " + year + "년 12월 31일";
        }
        dateList.add(startDate);
        dateList.add(endDate);
        dateList.add(saleTerm);
        return dateList;
    }
}
