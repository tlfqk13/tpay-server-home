package com.tpay.domains.vat.application;


import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import com.tpay.domains.order.application.CmsService;
import com.tpay.domains.order.application.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Service
@RequiredArgsConstructor
public class VatDownloadService {

    private final OrderService orderService;
    private final CmsService cmsService;
    private final FranchiseeFindService franchiseeFindService;
    private final FranchiseeUploadFindService franchiseeUploadFindService;
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

            List<Object> localDates = setUpDate(requestDate);
            LocalDate startDate = (LocalDate) localDates.get(0);
            LocalDate endDate = (LocalDate) localDates.get(1);
            String saleTerm = (String) localDates.get(2);
            //연월일
            //1. 제출자 인적사항
            boolean isMonthly = false;
            List<String> personalInfoResult = this.findPersonalInfo(franchiseeIndex, saleTerm,isMonthly);
            //2. 물품판매 총합계
            List<String> totalResult = orderService.findQuarterlyTotal(franchiseeIndex, startDate, endDate);
            //3. 물품판매 명세 (반기)
            List<List<String>> detailResult = orderService.findQuarterlyDetail(franchiseeIndex, startDate, endDate);

            // 최상단 (년 기(월))
            topSection(xssfWorkbook,sheet,requestDate,isMonthly);

            // 1. 제출자 인적사항
            // 데이터 ex ) sellerName, businessNumber, storeName, address, saleTerm, taxFreeStoreNumber
            personalInfoResultSection(xssfWorkbook,sheet,personalInfoResult);

            // 2. 물품판매 총합계
            // 데이터 ex ) totalCount, totalAmount, totalVat totalRefund
            totalResultSection(xssfWorkbook,sheet,totalResult);

            // 3. 물품판매 명세
            detailMonthlyResultSection(xssfWorkbook,sheet,detailResult);

            String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook);
            return result;

        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }
    }

    // TODO: 2022/07/08 가정 6월의 매출내역 메일을 7월 15일 새벽에 생성
    public void vatMonthlySendMailFile(Long franchiseeIndex, String requestMonth) {
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

            List<String> date = cmsService.setUpDate(requestMonth);
            String year = date.get(0);
            String month = date.get(1);
            String saleTerm = year + month;

            //1. 제출자 인적사항
            boolean isMonthly = true;
            List<String> personalInfoResult = this.findPersonalInfo(franchiseeIndex,saleTerm,isMonthly);
            //2. 물품판매 총합계
            List<String> totalResult = orderService.findMonthlyTotal(franchiseeIndex, year, month);
            //3. 물품판매 명세 (월)
            List<List<String>> detailMonthlyResult = orderService.findMonthlyDetail(franchiseeIndex, year, month);

            // 최상단 (년 기(월))
            topSection(xssfWorkbook,sheet,month,isMonthly);

            // 1. 제출자 인적사항
            // 데이터 ex ) sellerName, businessNumber, storeName, address, saleTerm, taxFreeStoreNumber
            personalInfoResultSection(xssfWorkbook,sheet,personalInfoResult);

            // 2. 물품판매 총합계
            // 데이터 ex ) totalCount, totalAmount, totalVat totalRefund
            totalResultSection(xssfWorkbook,sheet,totalResult);

            // 3. 물품판매 명세
            detailMonthlyResultSection(xssfWorkbook,sheet,detailMonthlyResult);

            StringBuilder fileName = new StringBuilder();
            fileName.append(personalInfoResult.get(2)).append("_").append(month).append("월").append("_실적명세서");
            String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook,fileName,month,false);

        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }
    }
    private void topSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, String requestDate,boolean isMonthly){
        XSSFRow topSectionRow = sheet.getRow(VatCustomValue.TOPSECTION_ROW);
        CellStyle topSectionCellStyle = cellStyleCustom(xssfWorkbook, false);
        topSectionRow.createCell(VatCustomValue.TOPSECTION_COLUMN, STRING).setCellStyle(topSectionCellStyle);
        if(isMonthly){
            topSectionRow.getCell(VatCustomValue.TOPSECTION_COLUMN).setCellValue("( 2022년" + requestDate + "기(월))");
        }else{
            String year = requestDate.substring(0, 2);
            String month = requestDate.substring(2);
            topSectionRow.getCell(VatCustomValue.TOPSECTION_COLUMN).setCellValue("( 20" + year + "년" + month + "기(월))");
        }
    }
    private void personalInfoResultSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, List<String> personalInfoResult) {
        CellStyle personalInfoResultCellStyle = cellStyleCustom(xssfWorkbook);
        CellStyle personalInfoResultCellStyle1 = cellStyleCustom(xssfWorkbook, true);
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
    private void totalResultSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, List<String> totalResult) {
        XSSFRow totalResultRow = sheet.getRow(VatCustomValue.TOTALRESULT_ROW);
        CellStyle personalInfoResultCellStyle = cellStyleCustom(xssfWorkbook);
        CellStyle costCellStyle = costCellStyleCustom(xssfWorkbook);
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
    private void detailMonthlyResultSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, List<List<String>> detailMonthlyResult) {
        for (int i = 0; i < detailMonthlyResult.size(); i++) {
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

    private CellStyle cellStyleCustom(XSSFWorkbook xssfWorkbook){
        CellStyle personalInfoResultCellStyle = xssfWorkbook.createCellStyle();
        personalInfoResultCellStyle.setBorderBottom(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderTop(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderLeft(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderRight(BorderStyle.THIN);
        personalInfoResultCellStyle.setAlignment(HorizontalAlignment.CENTER);

        return personalInfoResultCellStyle;
    }

    private CellStyle cellStyleCustom(XSSFWorkbook xssfWorkbook,boolean isTotalResultStyle){
        CellStyle personalInfoResultCellStyle = xssfWorkbook.createCellStyle();
        if (isTotalResultStyle) {
            personalInfoResultCellStyle.setBorderBottom(BorderStyle.THICK);
            personalInfoResultCellStyle.setAlignment(HorizontalAlignment.CENTER);
        }else{
            personalInfoResultCellStyle.setAlignment(HorizontalAlignment.CENTER);
        }
        return personalInfoResultCellStyle;
    }

    private CellStyle costCellStyleCustom(XSSFWorkbook xssfWorkbook){
        CellStyle personalInfoResultCellStyle = xssfWorkbook.createCellStyle();
        personalInfoResultCellStyle.setBorderBottom(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderTop(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderLeft(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderRight(BorderStyle.THIN);
        personalInfoResultCellStyle.setAlignment(HorizontalAlignment.RIGHT);

        return personalInfoResultCellStyle;
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

    private List<String> findPersonalInfo(Long franchiseeIndex, String saleTerm, boolean isMonthly) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        FranchiseeUploadEntity franchiseeUploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeIndex);
        List<String> result = new ArrayList<>();
        result.add(franchiseeEntity.getSellerName());
        result.add(NumberFormatConverter.addBarToBusinessNumber(franchiseeEntity.getBusinessNumber()));
        result.add(franchiseeEntity.getStoreName());
        result.add(franchiseeEntity.getStoreAddressBasic() + " " + franchiseeEntity.getStoreAddressDetail());
        if(isMonthly){
            result.add("20" + saleTerm.substring(2,4) + "년"
                    + saleTerm.substring(4,6) + "월 01일 ~ "
                    + saleTerm.substring(4,6) + "월 31일 " );
        }else{
            result.add(saleTerm); // 거래기간
        }
        result.add(franchiseeUploadEntity.getTaxFreeStoreNumber()); // 면세 판매장 지정번호
        return result;
    }

    public XSSFWorkbook vatDownloadsTest(Long franchiseeIndex, String requestDate) {
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
            System.out.println("################1");
            List<Object> localDates = setUpDate(requestDate);
            LocalDate startDate = (LocalDate) localDates.get(0);
            LocalDate endDate = (LocalDate) localDates.get(1);
            String saleTerm = (String) localDates.get(2);
            System.out.println("################2");
            XSSFRow row = sheet.createRow(12);
            XSSFRow row1 = sheet.getRow(11);
            row.createCell(11, STRING);
            row.createCell(5, STRING);
            row1.getCell(5).setCellValue("수신자만 바뀌었으면 성공");
            row.getCell(5).setCellValue("기존것도 바뀌는지 테스트"); //실패

            System.out.println("################3");


            //연월일
            //1. 제출자 인적사항
            boolean isMonthly = false;
           /* List<String> personalInfoResult = this.findPersonalInfo(franchiseeIndex, saleTerm, isMonthly);
            //2. 물품판매 총합계
            List<String> totalResult = orderService.findQuarterlyTotal(franchiseeIndex, startDate, endDate);
            //3. 물품판매 명세 (반기)
            List<List<String>> detailResult = orderService.findQuarterlyDetail(franchiseeIndex, startDate, endDate);

            // 최상단 (년 기(월))
            topSection(xssfWorkbook, sheet, requestDate, isMonthly);

            // 1. 제출자 인적사항
            // 데이터 ex ) sellerName, businessNumber, storeName, address, saleTerm, taxFreeStoreNumber
            personalInfoResultSection(xssfWorkbook, sheet, personalInfoResult);

            // 2. 물품판매 총합계
            // 데이터 ex ) totalCount, totalAmount, totalVat totalRefund
            totalResultSection(xssfWorkbook, sheet, totalResult);

            // 3. 물품판매 명세
            detailMonthlyResultSection(xssfWorkbook, sheet, detailResult);*/

           /* String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook);
            return result;*/

            /*FileOutputStream fileOutputStream = new FileOutputStream("/Users/tlfqk/Downloads/excelExportTest.xlsx", false);
            xssfWorkbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();*/

            return xssfWorkbook;

        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }
    }
}
