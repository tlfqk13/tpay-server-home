package com.tpay.domains.vat.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.DateFilter;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.vat.application.dto.VatDetailResponse;
import com.tpay.domains.vat.application.dto.VatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VatService {

    private final OrderService orderService;
    private final FranchiseeFindService franchiseeFindService;
    private final FranchiseeUploadFindService franchiseeUploadFindService;

    public VatResponse vatReport(Long franchiseeIndex, String requestDate) {
        List<Object> localDates = setUpDate(requestDate);
        LocalDate startDate = (LocalDate) localDates.get(0);
        LocalDate endDate = (LocalDate) localDates.get(1);
        return orderService.findQuarterlyVatReport(franchiseeIndex, startDate, endDate);
    }

    public VatDetailResponse vatDetail(Long franchiseeIndex, String requestDate) {
        List<Object> localDates = setUpDate(requestDate);
        LocalDate startDate = (LocalDate) localDates.get(0);
        LocalDate endDate = (LocalDate) localDates.get(1);
        String saleTerm = (String) localDates.get(2);
        //연월일
        //1. 제출자 인적사항
        List<String> personalInfoResult = this.findPersonalInfo(franchiseeIndex, saleTerm);
        //2. 물품판매 총합계
        List<String> totalResult = orderService.findQuarterlyTotal(franchiseeIndex, startDate, endDate);
        //3. 물품판매 명세
        boolean isMonthly = false;
        List<List<String>> detailResult = orderService.findQuarterlyDetail(franchiseeIndex, startDate, endDate);

        return VatDetailResponse.builder()
                .vatDetailResponsePersonalInfoList(personalInfoResult)
                .vatDetailResponseTotalInfoList(totalResult)
                .vatDetailResponseDetailInfoListList(detailResult)
                .build();
    }

    private List<Object> setUpDate(String requestDatePart) {
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

    public List<String> findPersonalInfo(Long franchiseeIndex, String saleTerm) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        FranchiseeUploadEntity franchiseeUploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeIndex);
        List<String> result = new ArrayList<>();
        result.add(franchiseeEntity.getSellerName());
        result.add(NumberFormatConverter.addBarToBusinessNumber(franchiseeEntity.getBusinessNumber()));
        result.add(franchiseeEntity.getStoreName());
        result.add(franchiseeEntity.getStoreAddressBasic() + " " + franchiseeEntity.getStoreAddressDetail());
        result.add(saleTerm);
        result.add(franchiseeUploadEntity.getTaxFreeStoreNumber());
        return result;
    }
}
