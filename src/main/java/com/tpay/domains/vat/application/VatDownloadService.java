package com.tpay.domains.vat.application;


import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.refund.application.RefundDetailFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Service
@RequiredArgsConstructor
@Slf4j
public class VatDownloadService {

    private final OrderService orderService;
    private final FranchiseeFindService franchiseeFindService;
    private final FranchiseeUploadFindService franchiseeUploadFindService;
    private final RefundDetailFindService refundDetailFindService;
    private final S3FileUploader s3FileUploader;

    public String vatDownloads(Long franchiseeIndex, String requestDate) {
        try {
            ClassPathResource resource = new ClassPathResource("KTP_REFUND_Form.xlsx");
            File file;
            try (InputStream inputStream = resource.getInputStream()) {
                file = File.createTempFile("KTP_REFUND_Form", "xlsx");
                FileUtils.copyInputStreamToFile(inputStream, file);
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0); // 첫번째 시트를 가져옴

            List<Object> localDates = this.setUpQuarterly(requestDate);
            LocalDate startLocalDate = (LocalDate) localDates.get(0);
            LocalDate endLocalDate = (LocalDate) localDates.get(1);

            log.trace(" @@ startLocalDate = {}", startLocalDate);
            log.trace(" @@ endLocalDate = {}", endLocalDate);

            System.out.println("requestDate-> " + requestDate);
            boolean isPaging = false;
            if (requestDate.length() == 3) {
                isPaging = true;
            }

            //연월일
            //1. 제출자 인적사항
            boolean isMonthly = false;
            List<String> personalInfoResult = this.findPersonalInfo(franchiseeIndex, startLocalDate, endLocalDate, isMonthly);
            //2. 물품판매 총합계
            List<String> totalResult = orderService.findCmsVatTotal(franchiseeIndex, startLocalDate, endLocalDate, RefundType.ALL, false);
            //3. 물품판매 명세 (반기)
            List<List<String>> detailResult = orderService.findCmsVatDetail(franchiseeIndex, startLocalDate, endLocalDate, isPaging, RefundType.ALL, false);

            // 최상단 (년 기(월))
            topSection(xssfWorkbook, sheet, startLocalDate, isMonthly);

            // 1. 제출자 인적사항
            // 데이터 ex ) sellerName, businessNumber, storeName, address, saleTerm, taxFreeStoreNumber
            personalInfoResultSection(xssfWorkbook, sheet, personalInfoResult, RefundType.ALL);

            // 2. 물품판매 총합계
            // 데이터 ex ) totalCount, totalAmount, totalVat totalRefund
            totalResultSection(xssfWorkbook, sheet, totalResult, RefundType.ALL);

            // 3. 물품판매 명세
            detailMonthlyResultSection(xssfWorkbook, sheet, detailResult, RefundType.ALL);

            StringBuilder fileName = new StringBuilder();
            fileName.append(personalInfoResult.get(2)).append("_").append("_실적명세서");
            String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook, fileName, false);
            return result;

        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }
    }

    public void vatAdminDownloads(String requestDate, RefundType refundType) {

        List<LocalDate> date = setUpDate(requestDate);
        LocalDate startLocalDate = date.get(0);
        LocalDate endLocalDate = date.get(1);

        String requestYearMonthly = String.valueOf(endLocalDate.getYear()).substring(2)
                + endLocalDate.getMonthValue();
        log.trace(" @@ requestYearMonthly = {}", requestYearMonthly);

        List<List<String>> totalResult = refundDetailFindService.findFranchiseeId(startLocalDate, endLocalDate);
        for (List<String> strings : totalResult) {
            this.vatMonthlySendMailFile(Long.valueOf(strings.get(0)), requestYearMonthly, refundType,true);
        }
    }

    public void vatAdminDownloadsQuarterly(String requestDate, RefundType refundType) {

        List<Object> localDates = this.setUpQuarterly(requestDate);
        LocalDate startLocalDate = (LocalDate) localDates.get(0);
        LocalDate endLocalDate = (LocalDate) localDates.get(1);

        List<List<String>> totalResult = refundDetailFindService.findFranchiseeId(startLocalDate, endLocalDate);
        for (List<String> strings : totalResult) {
            this.vatMonthlySendMailFile(Long.valueOf(strings.get(0)), requestDate, refundType, false);
        }
    }

    // ex 6월의 매출내역 메일을 7월 15일 새벽에 생성
    public void vatMonthlySendMailFile(Long franchiseeIndex, String requestMonth, RefundType refundType,boolean isMonthly) {
        try {
            ClassPathResource resource = new ClassPathResource("KTP_REFUND_Form.xlsx");
            if (RefundType.AFTER.equals(refundType)) {
                resource = new ClassPathResource("KTP_REFUND_AFTER_Form.xlsx");
            }
            File file;
            try (InputStream inputStream = resource.getInputStream()) {
                file = File.createTempFile("KTP_REFUND_Form", "xlsx");
                FileUtils.copyInputStreamToFile(inputStream, file);
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0); // 첫번째 시트를 가져옴

            List<LocalDate> date = setUpDate(requestMonth);
            LocalDate startLocalDate = date.get(0);
            LocalDate endLocalDate = date.get(1);

            if("222".equals(requestMonth) || "221".equals(requestMonth)){
                List<Object> localDates = this.setUpQuarterly(requestMonth);
                startLocalDate = (LocalDate) localDates.get(0);
                endLocalDate = (LocalDate) localDates.get(1);

                log.trace(" @@ setUpQuarterly_ startLocalDate = {}", startLocalDate);
                log.trace(" @@ setUpQuarterly_ endLocalDate = {}", endLocalDate);
            }

            //1. 제출자 인적사항
            List<String> personalInfoResult = this.findPersonalInfo(franchiseeIndex, startLocalDate, endLocalDate, isMonthly);
            //2. 물품판매 총합계
            List<String> totalResult = orderService.findCmsVatTotal(franchiseeIndex, startLocalDate, endLocalDate, refundType, false);
            //3. 물품판매 명세 (월)
            List<List<String>> detailMonthlyResult = orderService.findCmsVatDetail(franchiseeIndex, startLocalDate, endLocalDate, true, refundType, false);

            // 최상단 (년 기(월))
            topSection(xssfWorkbook, sheet, startLocalDate, isMonthly);

            // 1. 제출자 인적사항
            // 데이터 ex ) sellerName, businessNumber, storeName, address, saleTerm, taxFreeStoreNumber
            personalInfoResultSection(xssfWorkbook, sheet, personalInfoResult, refundType);

            // 2. 물품판매 총합계
            // 즉시 데이터 ex ) totalCount, totalAmount, totalVat totalRefund(즉시환급상당액)
            // 사후 데이터 ex ) totalCount, totalAmount, totalVat
            totalResultSection(xssfWorkbook, sheet, totalResult, refundType);

            // 3. 물품판매 명세
            // 즉시 데이터 ex ) 구매일련번호, 판매일자, 반출승인번호, 세금포함가격, 부가가치세, 환금금, 고객이름 , 고객 국적
            // 즉시 데이터 ex ) 구매일련번호, 판매일자, 반출일자, 반출승인번호, 환급(송금)일자, 환금금, 세금포함가격, 부가가치세
            detailMonthlyResultSection(xssfWorkbook, sheet, detailMonthlyResult, refundType);

            StringBuilder fileName = new StringBuilder();
            String typeName = "즉시 환급";
            String dateName = startLocalDate.getMonthValue() + "월_";
            if(RefundType.AFTER.equals(refundType)){
                typeName = "사후 환급";
            }

            if(!isMonthly){
                if(startLocalDate.getMonthValue() == 7){
                    dateName = "2 분기_";
                }else {
                    dateName = "1 분기_";
                }
            }

            fileName.append(personalInfoResult.get(2)).append("_").append(dateName).append(typeName).append("_실적명세서");
            String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook, fileName, String.valueOf(startLocalDate.getMonthValue()), false);

        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }
    }

    private void topSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, LocalDate startDate, boolean isMonthly) {
        XSSFRow topSectionRow = sheet.getRow(VatCustomValue.TOPSECTION_ROW);
        CellStyle topSectionCellStyle = cellStyleCustom(xssfWorkbook, false);
        String quarter = "";
        topSectionRow.createCell(VatCustomValue.TOPSECTION_COLUMN, STRING).setCellStyle(topSectionCellStyle);
        if (isMonthly) {
            topSectionRow.getCell(VatCustomValue.TOPSECTION_COLUMN).setCellValue("( 2022년" + startDate.getMonthValue() + "기(월))");
        } else {
            if (startDate.getMonthValue() == 1) {
                quarter = "1";
            } else {
                quarter = "2";
            }
            topSectionRow.getCell(VatCustomValue.TOPSECTION_COLUMN).setCellValue("( " + startDate.getYear() + "년" + quarter + "기(월))");
        }
    }

    private void personalInfoResultSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, List<String> personalInfoResult, RefundType refundType) {
        CellStyle personalInfoResultCellStyle = cellStyleCustom(xssfWorkbook);
        CellStyle personalInfoResultCellStyle1 = cellStyleCustom(xssfWorkbook, true);

        if (RefundType.AFTER.equals(refundType)) {
            XSSFRow personalInfoResultRow = sheet.getRow(VatCustomValue.PERSONALINFORESULT_ROW1);
            personalInfoResultRow.createCell(5, STRING).setCellStyle(personalInfoResultCellStyle);
            personalInfoResultRow.createCell(18, STRING).setCellStyle(personalInfoResultCellStyle);
            personalInfoResultRow.getCell(5).setCellValue(personalInfoResult.get(0));
            personalInfoResultRow.getCell(18).setCellValue(personalInfoResult.get(1));

            personalInfoResultRow = sheet.getRow(7);
            personalInfoResultRow.createCell(5, STRING).setCellStyle(personalInfoResultCellStyle);
            personalInfoResultRow.createCell(18, STRING).setCellStyle(personalInfoResultCellStyle);
            personalInfoResultRow.getCell(5).setCellValue(personalInfoResult.get(2));
            personalInfoResultRow.getCell(18).setCellValue(personalInfoResult.get(3));

            personalInfoResultRow = sheet.getRow(8);
            personalInfoResultRow.createCell(5, STRING).setCellStyle(personalInfoResultCellStyle);
            personalInfoResultRow.createCell(21, STRING).setCellStyle(personalInfoResultCellStyle);
            personalInfoResultRow.getCell(5).setCellValue(personalInfoResult.get(4));
            personalInfoResultRow.getCell(21).setCellValue(personalInfoResult.get(5));


        } else {
            for (int i = 0; i < 3; i++) {
                XSSFRow personalInfoResultRow = sheet.getRow(i + VatCustomValue.PERSONALINFORESULT_ROW1);
                if (i == 0 || i == 1) {
                    personalInfoResultRow.createCell(VatCustomValue.PERSONALINFORESULT_COLUMN_INDEX1, STRING).setCellStyle(personalInfoResultCellStyle);
                    personalInfoResultRow.createCell(VatCustomValue.PERSONALINFORESULT_COLUMN_INDEX2, STRING).setCellStyle(personalInfoResultCellStyle);
                    if (i == 0) {
                        personalInfoResultRow.getCell(VatCustomValue.PERSONALINFORESULT_COLUMN_INDEX1).setCellValue(personalInfoResult.get(0));
                        personalInfoResultRow.getCell(VatCustomValue.PERSONALINFORESULT_COLUMN_INDEX2).setCellValue(personalInfoResult.get(1));
                    } else {
                        personalInfoResultRow.getCell(VatCustomValue.PERSONALINFORESULT_COLUMN_INDEX1).setCellValue(personalInfoResult.get(2));
                        personalInfoResultRow.getCell(VatCustomValue.PERSONALINFORESULT_COLUMN_INDEX2).setCellValue(personalInfoResult.get(3));
                    }
                } else {
                    personalInfoResultRow.createCell(VatCustomValue.PERSONALINFORESULT_COLUMN_INDEX3, STRING).setCellStyle(personalInfoResultCellStyle1);
                    personalInfoResultRow.getCell(VatCustomValue.PERSONALINFORESULT_COLUMN_INDEX1).setCellValue(personalInfoResult.get(4));
                    personalInfoResultRow.getCell(VatCustomValue.PERSONALINFORESULT_COLUMN_INDEX3).setCellValue(personalInfoResult.get(5));
                }
            }
        }
    }

    private void totalResultSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, List<String> totalResult, RefundType refundType) {
        XSSFRow totalResultRow = sheet.getRow(VatCustomValue.TOTALRESULT_ROW);
        CellStyle personalInfoResultCellStyle = cellStyleCustom(xssfWorkbook);
        CellStyle costCellStyle = costCellStyleCustom(xssfWorkbook);

        if (RefundType.AFTER.equals(refundType)) {
            totalResultRow = sheet.getRow(VatCustomValue.TOTALRESULT_ROW - 1);
            totalResultRow.createCell(3, STRING).setCellStyle(personalInfoResultCellStyle); // 건수
            totalResultRow.createCell(7, STRING).setCellStyle(personalInfoResultCellStyle); // 판매금액
            totalResultRow.createCell(13, STRING).setCellStyle(personalInfoResultCellStyle); // 부가가치세
            totalResultRow.getCell(3).setCellValue(totalResult.get(0));
            totalResultRow.getCell(7).setCellValue(totalResult.get(1));
            totalResultRow.getCell(13).setCellValue(totalResult.get(2));
        } else {
            for (int i = 3; i <= 8; i++) {
                totalResultRow.createCell(i, STRING).setCellStyle(personalInfoResultCellStyle);
                if (i == 4 || i == 5 || i == 6) {
                    totalResultRow.createCell(i, STRING).setCellStyle(costCellStyle);
                    totalResultRow.getCell(i).setCellValue(totalResult.get(i - 3));
                }
            }
            totalResultRow.getCell(3).setCellValue(totalResult.get(0));
            totalResultRow.getCell(7).setCellValue("석세스모드");
            totalResultRow.getCell(8).setCellValue(VatCustomValue.SUCCESSMODE_BUSINESSNUMBER);
        }
    }

    private void detailMonthlyResultSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, List<List<String>> detailMonthlyResult, RefundType refundType) {
        System.out.println("detailMonthlyResultSection");
        System.out.println("detailMonthlyResult.size() -> " + detailMonthlyResult.size());

        if (RefundType.AFTER.equals(refundType)) {
            for (int i = 0; i < detailMonthlyResult.size(); i++) {
                System.out.println("i--> " + i);
                XSSFRow detailResultRow = sheet.getRow(i + VatCustomValue.DETAILRESULT_ROW - 1);
                CellStyle personalInfoResultCellStyle = cellStyleCustom(xssfWorkbook);
                CellStyle costCellStyle = costCellStyleCustom(xssfWorkbook);
                detailResultRow.getCell(1).setCellValue(i + 1);
                for (int j = 2; j <= 24; j++) {
                    if (j == 2) {
                        detailResultRow.createCell(j, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(0));
                    } else if (j == 4) {
                        detailResultRow.createCell(j, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(1)); // 판매일자
                    } else if (j == 6) {
                        detailResultRow.createCell(j, STRING).setCellStyle(costCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(8)); // 반출일자
                    } else if (j == 8) {
                        detailResultRow.createCell(j, STRING).setCellStyle(costCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(2)); // 반출승인번호
                    } else if (j == 11) {
                        detailResultRow.createCell(j, STRING).setCellStyle(costCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(9)); // 환급(송금)일자
                    } else {
                        detailResultRow.createCell(15, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(15).setCellValue(detailMonthlyResult.get(i).get(5)); // 환급액
                        detailResultRow.createCell(17, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(17).setCellValue(detailMonthlyResult.get(i).get(3)); // 판매금액
                        detailResultRow.createCell(19, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(19).setCellValue(detailMonthlyResult.get(i).get(4)); // 부가가치세
                        detailResultRow.createCell(20, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(20).setCellValue("0");
                        detailResultRow.createCell(22, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(22).setCellValue("0");
                        detailResultRow.createCell(24, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(24).setCellValue("0");
                    }
                }
            }

        } else {
            for (int i = 0; i < detailMonthlyResult.size(); i++) {
                System.out.println("i--> " + i);
                XSSFRow detailResultRow = sheet.getRow(i + VatCustomValue.DETAILRESULT_ROW);
                CellStyle personalInfoResultCellStyle = cellStyleCustom(xssfWorkbook);
                CellStyle costCellStyle = costCellStyleCustom(xssfWorkbook);
                detailResultRow.getCell(1).setCellValue(i + 1);
                for (int j = 2; j <= VatCustomValue.DETAILRESULT_ORGLEN; j++) {
                    if (j == 2) {
                        detailResultRow.createCell(j, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(0));
                    } else if (j == 5 || j == 6 || j == 7) {
                        detailResultRow.createCell(j, STRING).setCellStyle(costCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(j - 2));
                    } else {
                        detailResultRow.createCell(j, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(j - 2));
                    }
                }
            }
        }
    }

    private CellStyle cellStyleCustom(XSSFWorkbook xssfWorkbook) {
        CellStyle personalInfoResultCellStyle = xssfWorkbook.createCellStyle();
        personalInfoResultCellStyle.setBorderBottom(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderTop(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderLeft(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderRight(BorderStyle.THIN);
        personalInfoResultCellStyle.setAlignment(HorizontalAlignment.CENTER);

        return personalInfoResultCellStyle;
    }

    private CellStyle cellStyleCustom(XSSFWorkbook xssfWorkbook, boolean isTotalResultStyle) {
        CellStyle personalInfoResultCellStyle = xssfWorkbook.createCellStyle();
        if (isTotalResultStyle) {
            personalInfoResultCellStyle.setBorderBottom(BorderStyle.THICK);
            personalInfoResultCellStyle.setAlignment(HorizontalAlignment.CENTER);
        } else {
            personalInfoResultCellStyle.setAlignment(HorizontalAlignment.CENTER);
        }
        return personalInfoResultCellStyle;
    }

    private CellStyle costCellStyleCustom(XSSFWorkbook xssfWorkbook) {
        CellStyle personalInfoResultCellStyle = xssfWorkbook.createCellStyle();
        personalInfoResultCellStyle.setBorderBottom(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderTop(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderLeft(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderRight(BorderStyle.THIN);
        personalInfoResultCellStyle.setAlignment(HorizontalAlignment.RIGHT);

        return personalInfoResultCellStyle;
    }

    private List<Object> setUpQuarterly(String requestDatePart) {
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

    public List<LocalDate> setUpDate(String requestDate) {

        int yearInt = Integer.parseInt("20" + requestDate.substring(0, 2));
        int monthInt = Integer.parseInt(requestDate.substring(2));

        if (monthInt < 10) {
            monthInt = Integer.parseInt(requestDate.substring(2).replaceAll("0", ""));
        }

        LocalDate startDate = LocalDate.of(yearInt, monthInt, 1);
        LocalDate endDate;

        if (monthInt == 12) {
            endDate = LocalDate.of(yearInt + 1, 1, 1);
        } else {
            endDate = LocalDate.of(yearInt, monthInt + 1, 1);
        }

        startDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        endDate.format(DateTimeFormatter.BASIC_ISO_DATE);

        List<LocalDate> dateList = new ArrayList<>();
        endDate = endDate.minusDays(1);

        log.trace(" @@ setUpDate_ startDate = {}", startDate);
        log.trace(" @@ setUpDate_ endDate = {}", endDate);

        dateList.add(startDate);
        dateList.add(endDate);
        return dateList;
    }

    private List<String> findPersonalInfo(Long franchiseeIndex, LocalDate startDate, LocalDate endLocalDate,
                                          boolean isMonthly) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        FranchiseeUploadEntity franchiseeUploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeIndex);
        List<String> result = new ArrayList<>();
        result.add(franchiseeEntity.getSellerName());
        result.add(NumberFormatConverter.addBarToBusinessNumber(franchiseeEntity.getBusinessNumber()));
        result.add(franchiseeEntity.getStoreName());
        result.add(franchiseeEntity.getStoreAddressBasic() + " " + franchiseeEntity.getStoreAddressDetail());
        if (isMonthly) {
            result.add(startDate.getYear() + "년"
                    + startDate.getMonthValue() + "월 01일 ~ "
                    + startDate.getMonthValue() + "월 31일 ");
        } else {

            result.add(startDate.getYear() + "년"
                    + startDate.getMonthValue() + "월 01일 ~ "
                    + startDate.getMonthValue() + "월 31일 "

                    + endLocalDate.getYear() + "년"
                    + endLocalDate.getMonthValue() + "월 01일 ~ "
                    + endLocalDate.getMonthValue() + "월 31일 ");
        }
        result.add(franchiseeUploadEntity.getTaxFreeStoreNumber()); // 면세 판매장 지정번호
        return result;
    }
}
