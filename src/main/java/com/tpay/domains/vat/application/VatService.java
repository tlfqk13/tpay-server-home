package com.tpay.domains.vat.application;


import com.tpay.commons.aws.S3FileUploader;
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
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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
public class VatService {

    private final OrderService orderService;
    private final FranchiseeFindService franchiseeFindService;
    private final FranchiseeUploadFindService franchiseeUploadFindService;
    private final S3FileUploader s3FileUploader;

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

    public String vatDownloads(Long franchiseeIndex, String requestDate) {
        try {
            ClassPathResource resource = new ClassPathResource("KTP_CMS_REFUND_Form.xlsx");
            File file;
            try (InputStream inputStream = resource.getInputStream()) {
                file = File.createTempFile("KTP_CMS_REFUND_Form", "xlsx");
                FileUtils.copyInputStreamToFile(inputStream, file);
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0); // 첫번째 시트를 가져옴
            // TODO: 2022/07/06 글꼴, 크기 스타일(color) 는 아직 레퍼런스 없음
            // TODO: 2022/02/03 엑셀파일 일부 write 후 저장까지 테스트 완료 포멧에 맞게 입력하는 로직 정해지면 구현할 것

            List<Object> localDates = setUpDate(requestDate);
            LocalDate startDate = (LocalDate) localDates.get(0);
            LocalDate endDate = (LocalDate) localDates.get(1);
            String saleTerm = (String) localDates.get(2);
            //연월일
            //1. 제출자 인적사항
            List<String> personalInfoResult = this.findPersonalInfo(franchiseeIndex, saleTerm);
            //2. 물품판매 총합계
            List<String> totalResult = orderService.findQuarterlyTotal(franchiseeIndex, startDate, endDate);
            //3. 물품판매 명세 (반기)
            List<List<String>> detailResult = orderService.findQuarterlyDetail(franchiseeIndex, startDate, endDate);

            // 엑셀 테두리, 글꼴
            CellStyle topSectionCellStyle = cellStyleCustom(xssfWorkbook, false);
            CellStyle personalInfoResultCellStyle = cellStyleCustom(xssfWorkbook);
            CellStyle personalInfoResultCellStyle1 = cellStyleCustom(xssfWorkbook, true);
            CellStyle costCellStyle = costCellStyleCustom(xssfWorkbook);

            // 최상단 (년 기(월))
            XSSFRow topSectionRow = sheet.getRow(VatCustomValue.TOPSECTION_ROW);
            topSectionRow.createCell(VatCustomValue.TOPSECTION_COLUMN, STRING).setCellStyle(topSectionCellStyle);
            topSectionRow.getCell(VatCustomValue.TOPSECTION_COLUMN).setCellValue("( 20" + requestDate.substring(0, 2) + "년"
                    + requestDate.substring(2) + "기(월))");

            // 1. 제출자 인적사항
            // 데이터 ex ) sellerName, businessNumber, storeName, address, saleTerm, taxFreeStoreNumber
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

            // 2. 물품판매 총합계
            // 데이터 ex ) totalCount, totalAmount, totalVat
            XSSFRow totalResultRow = sheet.getRow(VatCustomValue.TOTALRESULT_ROW);

            for (int i = 3; i <= 8; i++) {
                totalResultRow.createCell(i, STRING).setCellStyle(personalInfoResultCellStyle);
                if (i == 4 || i == 5 || i == 6) {
                    totalResultRow.createCell(i, STRING).setCellStyle(costCellStyle);
                    totalResultRow.getCell(i).setCellValue(totalResult.get(i - 3));
                }
            }
            totalResultRow.getCell(3).setCellValue(totalResult.get(0));
            totalResultRow.getCell(7).setCellValue(personalInfoResult.get(1));
            totalResultRow.getCell(8).setCellValue(personalInfoResult.get(3));

            // 3. 물품판매 명세
            for (int i = 0; i < detailResult.size(); i++) {
                XSSFRow detailResultRow = sheet.getRow(i + VatCustomValue.DETAILRESULT_ROW);
                detailResultRow.createCell(1, STRING).setCellStyle(personalInfoResultCellStyle);
                detailResultRow.getCell(1).setCellValue(i + 1);
                for (int j = 2; j <= VatCustomValue.DETAILRESULT_ORGLEN; j++) {
                    if (j == 2) {
                        detailResultRow.createCell(j, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailResult.get(i).get(0));
                    } else if (j == 5 || j == 6 || j == 7) {
                        detailResultRow.createCell(j, STRING).setCellStyle(costCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailResult.get(i).get(j - 2));
                    } else {
                        detailResultRow.createCell(j, STRING).setCellStyle(personalInfoResultCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailResult.get(i).get(j - 2));
                    }
                }
            }

            //FileOutputStream fileOutputStream = new FileOutputStream("/Users/backend/excelExportTest.xlsx", false);
            //xssfWorkbook.write(fileOutputStream);
            //fileOutputStream.flush();
            //fileOutputStream.close();

            System.out.println("아웃풋스트림 생성 전");
            String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook);
            return result;

        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }
//    FranchiseeCmsResponseDetailInterface franchiseeCmsResponseDetailInterface = cmsDetail(franchiseeIndex, requestDate);
//    return "실패";
    }

    // TODO: 2022/07/08 가정 6월의 매출내역 메일을 7월 1일 새벽에 생성해서 7월 3일 새벽에 보내겠다.
    public String vatMonthlySendMailFile(Long franchiseeIndex, DateFilter dateFilter) {
        try {
            ClassPathResource resource = new ClassPathResource("KTP_CMS_REFUND_Form.xlsx");
            File file;
            try (InputStream inputStream = resource.getInputStream()) {
                file = File.createTempFile("KTP_CMS_REFUND_Form", "xlsx");
                FileUtils.copyInputStreamToFile(inputStream, file);
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0); // 첫번째 시트를 가져옴

            //연월일
            String month = dateFilter.getStartDate().toString().substring(5,7);
            //1. 제출자 인적사항
            List<String> personalInfoResult = this.findPersonalInfo(franchiseeIndex,month);
            //2. 물품판매 총합계
            List<String> totalResult = orderService.findQuarterlyTotal(franchiseeIndex, dateFilter.getStartDate(), dateFilter.getEndDate());
            //3. 물품판매 명세 (월)
            List<List<String>> detailMonthlyResult = orderService.findMonthlyDetail(franchiseeIndex, dateFilter.getStartDate(), dateFilter.getEndDate());

            // 엑셀 테두리, 글꼴
            CellStyle topSectionCellStyle = cellStyleCustom(xssfWorkbook, false);
            CellStyle personalInfoResultCellStyle = cellStyleCustom(xssfWorkbook);
            CellStyle personalInfoResultCellStyle1 = cellStyleCustom(xssfWorkbook, true);
            CellStyle costCellStyle = costCellStyleCustom(xssfWorkbook);

            // 최상단 (년 기(월))
            XSSFRow topSectionRow = sheet.getRow(VatCustomValue.TOPSECTION_ROW);
            topSectionRow.createCell(VatCustomValue.TOPSECTION_COLUMN, STRING).setCellStyle(topSectionCellStyle);
            topSectionRow.getCell(VatCustomValue.TOPSECTION_COLUMN).setCellValue("( 2022년" + month + "기(월))");

            // 1. 제출자 인적사항
            // 데이터 ex ) sellerName, businessNumber, storeName, address, saleTerm, taxFreeStoreNumber
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

            // 2. 물품판매 총합계
            // 데이터 ex ) totalCount, totalAmount, totalVat
            XSSFRow totalResultRow = sheet.getRow(VatCustomValue.TOTALRESULT_ROW);

            for (int i = 3; i <= 8; i++) {
                totalResultRow.createCell(i, STRING).setCellStyle(personalInfoResultCellStyle);
                if (i == 4 || i == 5 || i == 6) {
                    totalResultRow.createCell(i, STRING).setCellStyle(costCellStyle);
                    totalResultRow.getCell(i).setCellValue(totalResult.get(i - 3));
                }
            }
            totalResultRow.getCell(3).setCellValue(totalResult.get(0));
            totalResultRow.getCell(7).setCellValue(personalInfoResult.get(1));
            totalResultRow.getCell(8).setCellValue(personalInfoResult.get(3));

            // 3. 물품판매 명세
            for (int i = 0; i < detailMonthlyResult.size(); i++) {
                XSSFRow detailResultRow = sheet.getRow(i + VatCustomValue.DETAILRESULT_ROW);
                detailResultRow.createCell(1, STRING).setCellStyle(personalInfoResultCellStyle);
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

            //FileOutputStream fileOutputStream = new FileOutputStream("/Users/backend/excelExportTest.xlsx", false);
            //xssfWorkbook.write(fileOutputStream);
            //fileOutputStream.flush();
            //fileOutputStream.close();

            System.out.println("아웃풋스트림 생성 전");
            String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook);
            return result;

        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }
//    FranchiseeCmsResponseDetailInterface franchiseeCmsResponseDetailInterface = cmsDetail(franchiseeIndex, requestDate);
//    return "실패";
    }

    public CellStyle cellStyleCustom(XSSFWorkbook xssfWorkbook){
        CellStyle personalInfoResultCellStyle = xssfWorkbook.createCellStyle();
        personalInfoResultCellStyle.setBorderBottom(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderTop(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderLeft(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderRight(BorderStyle.THIN);
        personalInfoResultCellStyle.setAlignment(HorizontalAlignment.CENTER);

        return personalInfoResultCellStyle;
    }

    public CellStyle cellStyleCustom(XSSFWorkbook xssfWorkbook,boolean isTotalResultStyle){
        CellStyle personalInfoResultCellStyle = xssfWorkbook.createCellStyle();
        if (isTotalResultStyle) {
            personalInfoResultCellStyle.setBorderBottom(BorderStyle.THICK);
            personalInfoResultCellStyle.setAlignment(HorizontalAlignment.CENTER);
        }else{
            personalInfoResultCellStyle.setAlignment(HorizontalAlignment.CENTER);
        }
        return personalInfoResultCellStyle;
    }

    public CellStyle costCellStyleCustom(XSSFWorkbook xssfWorkbook){
        CellStyle personalInfoResultCellStyle = xssfWorkbook.createCellStyle();
        personalInfoResultCellStyle.setBorderBottom(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderTop(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderLeft(BorderStyle.THIN);
        personalInfoResultCellStyle.setBorderRight(BorderStyle.THIN);
        personalInfoResultCellStyle.setAlignment(HorizontalAlignment.RIGHT);

        return personalInfoResultCellStyle;
    }
}
