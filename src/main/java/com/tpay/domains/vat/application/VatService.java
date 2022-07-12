package com.tpay.domains.vat.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.UnknownException;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.vat.VatConst;
import com.tpay.domains.vat.application.dto.VatDetailResponse;
import com.tpay.domains.vat.application.dto.VatDetailResponseInterface;
import com.tpay.domains.vat.application.dto.VatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tpay.domains.vat.VatConst.*;

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

    private void homeTaxUploadTest(Long franchiseeIndex, String requestDate) {
        List<Object> localDates = setUpDate(requestDate);
        LocalDate startDate = (LocalDate) localDates.get(0);
        LocalDate endDate = (LocalDate) localDates.get(1);
        String saleTerm = (String) localDates.get(2);
    }

    private void createHomeTaxRecords(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);

        byte[] head = creatHeadRecord(franchiseeEntity, endDate);
        byte[] tail = createTailRecord(franchiseeEntity, endDate);
    }

    private byte[] creatHeadRecord(FranchiseeEntity franchiseeEntity, LocalDate endDate) {
        final int recordLen = RECORD_TOTAL_LENGTH.getLen();
        byte[] sendData = new byte[recordLen];
        Arrays.fill(sendData, (byte) ' ');

        fillCommonRecord(sendData, "IH", endDate, franchiseeEntity.getBusinessNumber());
        fillRecordIntoSendData(sendData, franchiseeEntity.getStoreName(), COMPANY_NAME);
        fillRecordIntoSendData(sendData, franchiseeEntity.getSellerName(), OWNER_NAME);
        fillRecordIntoSendData(sendData, "", OWNER_RESIDENT_OR_CORPORATION_NUMBER);
        fillRecordIntoSendData(sendData, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), WRITE_DATE);
        fillRecordIntoSendData(sendData, franchiseeEntity.getStoreTel(), TELEPHONE);

        return sendData;
    }

    private List<byte[]> createDataRecord(FranchiseeEntity franchiseeEntity, LocalDate startDate, LocalDate endDate) {


        List<byte[]> records = new ArrayList<>();

        List<VatDetailResponseInterface> details
                = orderService.findDetailBetweenDates(franchiseeEntity.getId(), startDate, endDate);

        final int recordLen = RECORD_TOTAL_LENGTH.getLen();
        for (VatDetailResponseInterface detail : details) {
            int index = 1;
            byte[] sendData = new byte[recordLen];
            Arrays.fill(sendData, (byte) ' ');

            fillCommonRecord(sendData, "ID", endDate, franchiseeEntity.getBusinessNumber());
            fillRecordIntoSendData(sendData, "0000", SUB_COMPANY_NUMBER);
            fillRecordIntoSendData(sendData, String.valueOf(index), SERIAL_NUMBER);
            fillRecordIntoSendData(sendData, detail.getPurchaseSerialNumber(), PURCHASE_SERIAL_NUMBER);
            fillRecordIntoSendData(sendData, detail.getSaleDate(), SUB_COMPANY_NUMBER);
            fillRecordIntoSendData(sendData, detail.getTakeoutConfirmNumber(), SUB_COMPANY_NUMBER);
            fillRecordIntoSendData(sendData, detail.getAmount(), SUB_COMPANY_NUMBER);
            fillRecordIntoSendData(sendData, detail.getVat(), SUB_COMPANY_NUMBER);
            fillRecordIntoSendData(sendData, detail.getRefundAmount(), SUB_COMPANY_NUMBER);
        }

        return records;
    }

    private byte[] createTailRecord(FranchiseeEntity franchiseeEntity, LocalDate endDate) {
        final int recordLen = RECORD_TOTAL_LENGTH.getLen();
        byte[] sendData = new byte[recordLen];

        fillCommonRecord(sendData, "IT", endDate, franchiseeEntity.getBusinessNumber());

        return sendData;
    }

    private void fillCommonRecord(byte[] sendData, String recordSection, LocalDate endTime, String bizNum) {
        fillRecordIntoSendData(sendData, recordSection, RECORD_SECTION);

        String year = String.valueOf(endTime.getYear());
        fillRecordIntoSendData(sendData, year, BELONGING_YEAR);

        String halfYearSection = endTime.getMonthValue() <= 6 ? String.valueOf(1) : String.valueOf(2);
        fillRecordIntoSendData(sendData, halfYearSection, HALF_YEAR_SECTION);

        String monthInHalf
                = endTime.getMonthValue() == 6 || endTime.getMonthValue() == 12 ? String.valueOf(6) : String.valueOf(3);
        fillRecordIntoSendData(sendData, monthInHalf, MONTH_IN_HALF_YEAR);

        fillRecordIntoSendData(sendData, bizNum, BUSINESS_REG_NUMBER);
    }

    private void fillRecordIntoSendData(byte[] sendData, String data, VatConst vatConst) {
        if (data.isEmpty() && vatConst.isNeed()) {
                throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Record data not exists");
        }

        int startPos = vatConst.getStart();
        if (vatConst.getLen() == data.length()) {
            byte[] byteData = convertByteArrayUsingCharset(data, "EUC-KR");
            System.arraycopy(byteData, 0, sendData, startPos, byteData.length);
        } else {
            if (vatConst.getConstType() == VatConstType.CHAR) {
                setDataWithRightWhiteSpacePadding(sendData, data, vatConst);
            } else {
                setDataWithLeftZeroPadding(sendData, data, vatConst);
            }
        }
    }

    private void setDataWithLeftZeroPadding(byte[] sendData, String data, VatConst vatConst) {
        byte[] byteData = convertByteArrayUsingCharset(data, "EUC-KR");

        int paddingLen = vatConst.getLen() - byteData.length;
        int startPos = vatConst.getStart();
        int endPaddingPos = startPos + paddingLen;

        for (int i = startPos; i < endPaddingPos; ++i) {
            sendData[i] = '0';
        }

        System.arraycopy(byteData, 0, sendData, endPaddingPos, byteData.length);
    }

    private void setDataWithRightWhiteSpacePadding(byte[] sendData, String data, VatConst vatConst) {
        byte[] byteData = convertByteArrayUsingCharset(data, "EUC-KR");

        int startPos = vatConst.getStart();
        System.arraycopy(byteData, 0, sendData, startPos, byteData.length);

        int startPaddingPos = startPos + byteData.length;
        int endPaddingPos = startPos + vatConst.getLen();
        for (int i = startPaddingPos; i < endPaddingPos; ++i) {
            sendData[i] = ' ';
        }
    }

    private byte[] convertByteArrayUsingCharset(String data, String charsetName) {
        data = (data == null ? "" : data);

        byte[] byteData = null;
        try {
            byteData = data.getBytes(charsetName);
        } catch (UnsupportedEncodingException exception) {
            throw new UnknownException(
                    ExceptionState.UNKNOWN, "Failed to convert byte array in " + charsetName);
        }
        return byteData;
    }
}
