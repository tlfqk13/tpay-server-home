package com.tpay.domains.vat.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.UnknownException;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.vat.HomeTaxConstant;
import com.tpay.domains.vat.application.dto.VatDetailResponseInterface;
import com.tpay.domains.vat.application.dto.VatHomeTaxDto;
import com.tpay.domains.vat.application.dto.VatTotalResponseInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tpay.domains.vat.HomeTaxConstant.*;
import static com.tpay.domains.vat.HomeTaxConstant.BUSINESS_REG_NUMBER;

@Slf4j
@Service
@RequiredArgsConstructor
public class VatHomeTaxService {

    private final OrderService orderService;
    private final FranchiseeFindService franchiseeFindService;
    private final FranchiseeUploadFindService franchiseeUploadFindService;

    private static final String TEST_BIZ_NUM = "2390401226";

    // TODO - 추후 법인이며, 종사업을 거느린 사업자의 경우 종사업자 일련번호(SUB_COMPANY_NUMBER)를 받아서 처리해야함
    private static final String TEMP_SUB_COMPANY_NUM = "0000";

    // TODO 주민번호 앞자리 또는 법인등록번호로 레코드 만드는 부분 추가 필요(OWNER_RESIDENT_OR_CORPORATION_NUMBER)
    private static final String TEMP_CORP_NUM = "7777777777";
    private static final String CHARSET = "EUC-KR";
    private static final String REFUND_CORP_NUM = "2390401226";

    public VatHomeTaxDto.Response createHomeTaxUploadFile(Long franchiseeIndex, String requestDate) {
        List<LocalDate> localDates = setUpDate(requestDate);
        LocalDate startDate = localDates.get(0);
        LocalDate endDate = localDates.get(1);

        createHomeTaxRecords(franchiseeIndex, startDate, endDate);

        return new VatHomeTaxDto.Response(false);
    }

    private List<LocalDate> setUpDate(String requestDatePart) {
        List<LocalDate> dateList = new ArrayList<>();
        String requestDate = "20" + requestDatePart;
        String year = requestDate.substring(0, 4);
        String halfOfYear = requestDate.substring(4);
        LocalDate startDate;
        LocalDate endDate;
        if (!(requestDate.length() == 5 && (halfOfYear.equals("1") || halfOfYear.equals("2")))) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid request data format");
        }

        if (halfOfYear.equals("1")) {
            startDate = LocalDate.parse(year + "-01-01");
            endDate = LocalDate.parse(year + "-06-30");
        } else {
            startDate = LocalDate.parse(year + "-07-01");
            endDate = LocalDate.parse(year + "-12-31");
        }
        dateList.add(startDate);
        dateList.add(endDate);
        return dateList;
    }

    private void createHomeTaxRecords(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);

        if (!franchiseeEntity.getIsRefundOnce()) {
            log.error("환급이 발생되지 않은 고객입니다");
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "환급을 받은 기록이 없는 사용자입니다");
        }

        log.trace("홈택스 레코드 생성 시작");
        byte[] head = creatHeadRecord(franchiseeEntity, endDate);
        List<byte[]> dataRecords = createDataRecord(franchiseeEntity, startDate, endDate);
        byte[] tail = createTailRecord(franchiseeEntity, startDate, endDate);

        StringBuilder dataString = new StringBuilder();
        dataString.append(convertByteArrToString(head));
        dataString.append("\r\n");
        for (byte[] dataRecord : dataRecords) {
            dataString.append(convertByteArrToString(dataRecord));
            dataString.append("\r\n");
        }
        dataString.append(convertByteArrToString(tail));
        log.trace("홈택스 레코드 생성 완료");

        // I(전자신고용 변환파일임을 나타냄) + 사업자등록번호 + V178(서식코드)
        String fileName = "I" + franchiseeEntity.getBusinessNumber() + ".V178";
        //        String fileName = "I" + TEST_BIZ_NUM + ".V178";
        byte[] homeTaxUploadData = convertByteArrayUsingCharset(dataString.toString());

        // TODO 데이터 저장 위치와 형태, 제목 설정하고 저장하는 로직 추가
        
//        File file = new File("/home/ec2-user/testdata/" + fileName);
//        try (FileOutputStream outStream = new FileOutputStream(file)) {
//            outStream.write(convertByteArrayUsingCharset(dataString.toString()));
//            log.trace("Home tax upload file save success = {} ", fileName);
//        } catch (IOException e) {
//            log.error("Home tax upload file saved failed");
//            throw new UnknownException(ExceptionState.UNKNOWN, "홈택스 파일 쓰기 실패");
//        }
        
    }
    
    private byte[] creatHeadRecord(FranchiseeEntity franchiseeEntity, LocalDate endDate) {
        final int recordLen = RECORD_TOTAL_LENGTH.getLen();
        byte[] sendData = new byte[recordLen];
        Arrays.fill(sendData, (byte) ' ');

        final String HEAD_RECORD_SECTION = "IH";
        fillCommonRecord(sendData, HEAD_RECORD_SECTION, endDate, franchiseeEntity.getBusinessNumber());
        fillRecordIntoSendData(sendData, franchiseeEntity.getStoreName(), STORE_NAME);
        fillRecordIntoSendData(sendData, franchiseeEntity.getSellerName(), OWNER_NAME);
        fillRecordIntoSendData(sendData, TEMP_CORP_NUM, OWNER_RESIDENT_OR_CORPORATION_NUMBER);
        fillRecordIntoSendData(sendData, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), WRITE_DATE);
        fillRecordIntoSendData(sendData, franchiseeEntity.getStoreTel(), STORE_TEL);

        return sendData;
    }

    private List<byte[]> createDataRecord(FranchiseeEntity franchiseeEntity, LocalDate startDate, LocalDate endDate) {
        List<byte[]> records = new ArrayList<>();

        List<VatDetailResponseInterface> details
                = orderService.findDetailBetweenDates(franchiseeEntity.getId(), startDate, endDate);

        if(details.isEmpty()){
            log.error("기간 내 환급 내역이 존재하지 않습니다.");
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "기간 내 환급 내역 존재하지 않음");
        }

        final int recordLen = RECORD_TOTAL_LENGTH.getLen();
        int serialIndex = 1;
        for (VatDetailResponseInterface detail : details) {
            byte[] sendData = new byte[recordLen];
            Arrays.fill(sendData, (byte) ' ');

            final String DATA_RECORD_SECTION = "ID";
            fillCommonRecord(sendData, DATA_RECORD_SECTION, endDate, franchiseeEntity.getBusinessNumber());
            fillRecordIntoSendData(sendData, TEMP_SUB_COMPANY_NUM, SUB_COMPANY_NUMBER);
            fillRecordIntoSendData(sendData, String.valueOf(serialIndex++), SERIAL_NUMBER);
            fillRecordIntoSendData(sendData, detail.getPurchaseSerialNumber(), PURCHASE_SERIAL_NUMBER);
            fillRecordIntoSendData(sendData, detail.getSaleDate(), SALE_DATE);
            fillRecordIntoSendData(sendData, detail.getTakeoutConfirmNumber(), CARRY_OUT_PERMISSION_NUMBER);
            fillRecordIntoSendData(sendData, detail.getAmount(), PRICE_WITH_TAX);
            fillRecordIntoSendData(sendData, detail.getVat(), VAT);
            fillRecordIntoSendData(sendData, detail.getRefundAmount(), TAX_REFUND_AMOUNT);
            fillRecordIntoSendData(sendData, detail.getCustomerName(), BUYER_NAME);
            fillRecordIntoSendData(sendData, detail.getCustomerNational(), BUYER_COUNTRY);
            records.add(sendData);
        }

        return records;
    }

    private byte[] createTailRecord(FranchiseeEntity franchiseeEntity, LocalDate startDate, LocalDate endDate) {
        final int recordLen = RECORD_TOTAL_LENGTH.getLen();
        byte[] sendData = new byte[recordLen];
        Arrays.fill(sendData, (byte) ' ');

        VatTotalResponseInterface total = orderService.findTotalBetweenDates(franchiseeEntity.getId(), startDate, endDate);
        FranchiseeUploadEntity uploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeEntity.getId());

        final String TAIL_RECORD_SECTION = "IT";
        fillCommonRecord(sendData, TAIL_RECORD_SECTION, endDate, franchiseeEntity.getBusinessNumber());
        fillRecordIntoSendData(sendData, TEMP_CORP_NUM, SUB_COMPANY_NUMBER);
        fillRecordIntoSendData(sendData, uploadEntity.getTaxFreeStoreNumber(), TAX_FREE_STORE_NUMBER);
//        fillRecordIntoSendData(sendData, "12341234", TAX_FREE_STORE_NUMBER); // test
        fillRecordIntoSendData(sendData, total.getTotalCount(), TOTAL_COUNT);
        fillRecordIntoSendData(sendData, total.getTotalAmount(), TOTAL_PRICE_WITH_TAX);
        fillRecordIntoSendData(sendData, total.getTotalVat(), TOTAL_VAT);
        fillRecordIntoSendData(sendData, total.getTotalRefund(), TOTAL_TAX_REFUND_AMOUNT);
        fillRecordIntoSendData(sendData, REFUND_CORP_NUM, REFUND_BUSINESS_NUMBER);

        return sendData;
    }

    private void fillCommonRecord(byte[] sendData, String recordSection, LocalDate endTime, String bizNum) {
        fillRecordIntoSendData(sendData, recordSection, RECORD_SECTION);

        String year = String.valueOf(endTime.getYear());
        fillRecordIntoSendData(sendData, year, BELONGING_YEAR);

        String halfYearSection = endTime.getMonthValue() <= 6 ? String.valueOf(1) : String.valueOf(2);
        fillRecordIntoSendData(sendData, halfYearSection, HALF_YEAR_SECTION);

        // 반기내 월 순번은 보통 6(확정)으로 6월, 12월까지의 자료로 작성
        // 하지만 법인의 경우 3(예정)으로 2번의 추가 신고가 존재, 따라서 6월, 12월 데이터가 아닌 경우 3(예정)으로 진행하게 로직
        // 보다 정확한 구별을 위해서는 파라미터로 받아서 사용하는 것도 좋을듯
        String monthInHalf
                = endTime.getMonthValue() == 6 || endTime.getMonthValue() == 12 ? String.valueOf(6) : String.valueOf(3);
        fillRecordIntoSendData(sendData, monthInHalf, MONTH_IN_HALF_YEAR);

        fillRecordIntoSendData(sendData, bizNum, BUSINESS_REG_NUMBER);
//        fillRecordIntoSendData(sendData, TEST_BIZ_NUM, BUSINESS_REG_NUMBER); // test
    }

    private void fillRecordIntoSendData(byte[] sendData, String data, HomeTaxConstant homeTaxConstant) {
        if (data == null) {
            data = "";
        }
        if (data.isEmpty() && homeTaxConstant.isNeed()) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Record data not exists");
        }

        int startPos = homeTaxConstant.getStart();
        if (homeTaxConstant.getLen() == data.length()) {
            byte[] byteData = convertByteArrayUsingCharset(data);
            System.arraycopy(byteData, 0, sendData, startPos, byteData.length);
        } else {
            if (homeTaxConstant.getConstType() == VatConstType.CHAR) {
                setDataWithRightWhiteSpacePadding(sendData, data, homeTaxConstant);
            } else {
                setDataWithLeftZeroPadding(sendData, data, homeTaxConstant);
            }
        }
    }

    private void setDataWithLeftZeroPadding(byte[] sendData, String data, HomeTaxConstant homeTaxConstant) {
        byte[] byteData = convertByteArrayUsingCharset(data);

        int paddingLen = homeTaxConstant.getLen() - byteData.length;
        int startPos = homeTaxConstant.getStart();
        int endPaddingPos = startPos + paddingLen;

        for (int i = startPos; i < endPaddingPos; ++i) {
            sendData[i] = '0';
        }

        System.arraycopy(byteData, 0, sendData, endPaddingPos, byteData.length);
    }

    private void setDataWithRightWhiteSpacePadding(byte[] sendData, String data, HomeTaxConstant homeTaxConstant) {
        byte[] byteData = convertByteArrayUsingCharset(data);

        int startPos = homeTaxConstant.getStart();
        System.arraycopy(byteData, 0, sendData, startPos, byteData.length);

        int startPaddingPos = startPos + byteData.length;
        int endPaddingPos = startPos + homeTaxConstant.getLen();
        for (int i = startPaddingPos; i < endPaddingPos; ++i) {
            sendData[i] = ' ';
        }
    }

    private byte[] convertByteArrayUsingCharset(String data) {
        data = (data == null ? "" : data);

        byte[] byteData = null;
        try {
            byteData = data.getBytes(CHARSET);
        } catch (UnsupportedEncodingException exception) {
            throw new UnknownException(
                    ExceptionState.UNKNOWN, "Failed to convert byte array in " + CHARSET);
        }
        return byteData;
    }

    private String convertByteArrToString(byte[] bytes) {

        String data = null;
        try {
            data = new String(bytes, CHARSET);
        } catch (UnsupportedEncodingException exception) {
            throw new UnknownException(
                    ExceptionState.UNKNOWN, "Failed to convert byte array to String");
        }

        return data;
    }
}