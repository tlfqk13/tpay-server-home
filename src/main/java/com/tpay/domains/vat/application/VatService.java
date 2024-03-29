package com.tpay.domains.vat.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.vat.application.dto.VatDetailDto;
import com.tpay.domains.vat.application.dto.VatDetailResponse;
import com.tpay.domains.vat.application.dto.VatTotalDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class VatService {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final FranchiseeUploadFindService franchiseeUploadFindService;

    public VatTotalDto.Response vatReport(Long franchiseeIndex, String requestDate) {
        List<Object> localDates = setUpDate(requestDate);
        LocalDate startLocalDate = (LocalDate) localDates.get(0);
        LocalDate endLocalDate = (LocalDate) localDates.get(1);

        VatTotalDto.Response vatTotalResponse  = orderRepository.findMonthlyTotal(franchiseeIndex, startLocalDate, endLocalDate, RefundType.ALL);
        if (vatTotalResponse == null) {
            return VatTotalDto.Response.builder().totalAmount("0").totalCount("0").totalVat("0").totalCommission("0").build();
        }
        return VatTotalDto.Response.builder()
                .totalCount(vatTotalResponse.getTotalCount())
                .totalAmount(vatTotalResponse.getTotalAmount())
                .totalRefund(vatTotalResponse.getTotalRefund())
                .totalVat(vatTotalResponse.getTotalVat())
                .totalSupply(vatTotalResponse.getTotalSupply())
                .build();
    }

    public VatDetailResponse vatDetail(Long franchiseeIndex, String requestDate) {
        List<Object> localDates = setUpDate(requestDate);
        LocalDate startLocalDate = (LocalDate) localDates.get(0);
        LocalDate startEndDate = (LocalDate) localDates.get(1);
        String saleTerm = (String) localDates.get(2);
        //연월일
        //1. 제출자 인적사항
        List<String> personalInfoResult = this.findPersonalInfo(franchiseeIndex, saleTerm);
        //2. 물품판매 총합계
        List<String> totalResult = orderService.findCmsVatTotal(franchiseeIndex, startLocalDate, startEndDate, RefundType.ALL, false);
        //3. 물품판매 명세
        List<List<String>> detailResult = this.findCmsVatDetailFromApp(franchiseeIndex, startLocalDate, startEndDate);

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

    private List<List<String>> findCmsVatDetailFromApp(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate) {

        int pageData = 100;

        List<VatDetailDto.Response> vatDetailResponse = orderRepository.findMonthlyCmsVatDetail(franchiseeIndex, startLocalDate, endLocalDate,pageData, RefundType.ALL);
        List<List<String>> detailResult = new ArrayList<>();

        for (VatDetailDto.Response response : vatDetailResponse) {
            List<String> baseList = new ArrayList<>();
            baseList.add(response.getPurchaseSerialNumber());
            baseList.add(String.valueOf(response.getSaleDate()));
            baseList.add(response.getTakeOutConfirmNumber());
            baseList.add(NumberFormatConverter.addCommaToNumber(response.getAmount()));
            baseList.add(NumberFormatConverter.addCommaToNumber(response.getVat()));
            baseList.add(NumberFormatConverter.addCommaToNumber(response.getRefundAmount()));
            detailResult.add(baseList);
        }
        return detailResult;
    }
}
