package com.tpay.domains.order.application;


import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.order.application.dto.CmsDetailResponse;
import com.tpay.domains.order.application.dto.CmsResponse;
import com.tpay.domains.order.application.dto.CmsResponseDetailInterface;
import com.tpay.domains.order.application.dto.CmsResponseInterface;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.refund.application.RefundDetailFindService;
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
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Service
@RequiredArgsConstructor
public class CmsService {

    private final OrderRepository orderRepository;
    private final S3FileUploader s3FileUploader;
    private final OrderService orderService;
    private final FranchiseeFindService franchiseeFindService;

    private final FranchiseeUploadFindService franchiseeUploadFindService;
    private final RefundDetailFindService refundDetailFindService;

    public CmsResponse cmsReport(Long franchiseeIndex, String requestDate) {
        List<String> date = setUpDate(requestDate);
        String year = date.get(0);
        String month = date.get(1);
        CmsResponseInterface queryResult = orderRepository.findMonthlyCmsReport(franchiseeIndex, year, month);
        if (queryResult == null) {
            return CmsResponse.builder().totalAmount("0").totalCount("0").totalVat("0").totalCommission("0").build();
        }

        return CmsResponse.builder()
            .totalCount(queryResult.getTotalCount())
            .totalAmount(queryResult.getTotalAmount())
            .totalCommission(queryResult.getTotalCommission())
            .totalVat(queryResult.getTotalVat())
            .build();

    }

    public CmsDetailResponse cmsDetail(Long franchiseeIndex, String requestDate) {
        List<String> date = setUpDate(requestDate);
        String year = date.get(0);
        String month = date.get(1);
        CmsResponseDetailInterface queryResult = orderRepository.findMonthlyCmsDetail(franchiseeIndex, year, month);
        if (queryResult == null) {
            return CmsDetailResponse.builder()
                .commissionInfoList(Arrays.asList("0", "0", "0", "0"))
                .customerInfoList(Arrays.asList("", "", "", "", "0")).build();
        }
        List<String> commissionInfoList = new ArrayList<>();
        List<String> customerInfoList = new ArrayList<>();
        commissionInfoList.add(NumberFormatConverter.addCommaToNumber(queryResult.getTotalCount()) + "건");
        commissionInfoList.add(NumberFormatConverter.addCommaToNumber(queryResult.getTotalAmount()));
        commissionInfoList.add(NumberFormatConverter.addCommaToNumber(queryResult.getTotalVat()));
        commissionInfoList.add(NumberFormatConverter.addCommaToNumber(queryResult.getTotalCommission()));

        customerInfoList.add(queryResult.getSellerName());
        customerInfoList.add(queryResult.getBankName());
        customerInfoList.add(queryResult.getAccountNumber());
        customerInfoList.add(queryResult.getWithdrawalDate() + "일");
        customerInfoList.add(NumberFormatConverter.addCommaToNumber(queryResult.getTotalBill()));
        return CmsDetailResponse.builder().commissionInfoList(commissionInfoList).customerInfoList(customerInfoList).build();
    }

    public void cmsDownloads(Long franchiseeIndex, String requestDate) {
        try {
            ClassPathResource resource = new ClassPathResource("KTP_CMS_Form.xlsx");
            File file1;
            try (InputStream inputStream = resource.getInputStream()) {
                file1 = File.createTempFile("KTP_CMS_Form", "xlsx");
                FileUtils.copyInputStreamToFile(inputStream, file1);
            }
            FileInputStream fileInputStream = new FileInputStream(file1);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFSheet sheet1 = xssfWorkbook.getSheetAt(1);
            // TODO: 2022/02/03 엑셀파일 일부 write 후 저장까지 테스트 완료 포멧에 맞게 입력하는 로직 정해지면 구현할 것
            // TODO: 2022/07/14 CMS 청구내역 엑셀파일 양식 새롭게 받음.

            List<String> date = this.setUpDate(requestDate);
            String year = date.get(0);
            String month = date.get(1);
            // 1. 물품판매 상세내역
            boolean isPaging  = true;
            List<List<String>> detailMonthlyResult = orderService.findMonthlyCmsDetail(franchiseeIndex, year, month,false);
            // 2. 물품판매 총합계
            List<String> totalResult = orderService.findMonthlyTotal(franchiseeIndex, year, month);
            // TopSection
            List<String> topSectionInfo = this.topSectionInfo(franchiseeIndex, year,month);

            // 최상단 영역
            topSection(sheet,topSectionInfo);
            // [매장명] [날짜] 영역
            secondSection(xssfWorkbook,sheet,topSectionInfo);
            // 총 건수 . 청구 금액
            totalResultRow(sheet,totalResult);

            if(detailMonthlyResult.size() == 15){
                // 물품상세 내역
                detailResultRow(xssfWorkbook,sheet,detailMonthlyResult,totalResult,false);
                detailMonthlyResult = orderService.findMonthlyCmsDetail(franchiseeIndex, year, month,isPaging);
                detailResultRow(xssfWorkbook,sheet1,detailMonthlyResult,totalResult,isPaging);
            }else{
                detailMonthlyResult = orderService.findMonthlyDetail(franchiseeIndex, year, month);
                detailResultRow(xssfWorkbook,sheet,detailMonthlyResult,totalResult,false);
            }

           StringBuilder fileName = new StringBuilder();
           fileName.append(topSectionInfo.get(2)).append("_").append(month).append("월").append("_cms");
           boolean isCms = true;
           String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook,fileName,month,isCms);

        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }
    }

    public void cmsAdminDownloads() {
        String requestYear = String.valueOf(LocalDate.now().getYear()).substring(2);
        String requestMonthly = String.valueOf(LocalDate.now().getMonthValue());
        String requestYearMonthly = requestYear + requestMonthly;

        List<String> date = this.setUpDate(requestYearMonthly);
        String year = date.get(0);
        String month = date.get(1);
        List<List<String>> totalResult = refundDetailFindService.findFranchiseeId(year, month);
        for (List<String> strings : totalResult) {
            this.cmsDownloads(Long.valueOf(strings.get(0)), requestYearMonthly);
        }
    }

    public List<String> setUpDate(String requestDatePart) {

        int yearInt = Integer.parseInt("20" + requestDatePart.substring(0, 2));
        int monthInt = Integer.parseInt(requestDatePart.substring(2).replaceAll("0", ""));

        LocalDate localDate = LocalDate.of(yearInt,monthInt,1);
        LocalDate localDate1 = localDate.minusMonths(1);
        String year = String.valueOf(localDate1.getYear());
        String month = String.valueOf(localDate1.getMonthValue());
        if(month.length() == 1) {
            month = "0" + month;
        }

        List<String> dateList = new ArrayList<>();
        dateList.add(year);
        dateList.add(month);
        return dateList;
    }
    private void topSection(XSSFSheet sheet, List<String> topSectionInfo) {
        for(int i=CmsCustomValue.TOPSECTION_STARTROW; i<=CmsCustomValue.TOPSECTION_ENDROW; i+=2){
            XSSFRow topSection = sheet.getRow(i);
            if(i == 8){
                topSection.createCell(CmsCustomValue.TOPSECTION_FIRSTCELL);
                topSection.createCell(CmsCustomValue.TOPSECTION_SECONDCELL);
                topSection.getCell(CmsCustomValue.TOPSECTION_FIRSTCELL).setCellValue(topSectionInfo.get(0));
                topSection.getCell(CmsCustomValue.TOPSECTION_SECONDCELL).setCellValue(topSectionInfo.get(1));
            }else if(i == 10){
                topSection.createCell(CmsCustomValue.TOPSECTION_FIRSTCELL);
                topSection.getCell(CmsCustomValue.TOPSECTION_FIRSTCELL).setCellValue(topSectionInfo.get(2));
            }else if(i == 12){
                topSection.createCell(CmsCustomValue.TOPSECTION_FIRSTCELL);
                topSection.getCell(CmsCustomValue.TOPSECTION_FIRSTCELL).setCellValue("참조");
            }else{
                topSection.createCell(CmsCustomValue.TOPSECTION_FIRSTCELL);
                topSection.getCell(CmsCustomValue.TOPSECTION_FIRSTCELL).setCellValue(topSectionInfo.get(3));
            }
        }
    }
    private void totalResultRow(XSSFSheet sheet, List<String> totalResult){
        XSSFRow totalResultRow = sheet.getRow(CmsCustomValue.TOTALRESULT_ROW);
        totalResultRow.createCell(CmsCustomValue.TOTALRESULT_FIRSTCELL);
        totalResultRow.createCell(CmsCustomValue.TOTALRESULT_SECONDCELL);
        totalResultRow.getCell(CmsCustomValue.TOTALRESULT_FIRSTCELL).setCellValue(totalResult.get(0));
        totalResultRow.getCell(CmsCustomValue.TOTALRESULT_SECONDCELL).setCellValue(totalResult.get(3));
    }
    private void secondSection(XSSFWorkbook xssfWorkbook,XSSFSheet sheet, List<String> topSectionInfo) {
        XSSFRow secondSection = sheet.getRow(CmsCustomValue.SECONDSECTION_ROW);
        CellStyle secondSectionCellStyle = cellStyleCustom(xssfWorkbook);
        secondSection.createCell(CmsCustomValue.SECONDSECTION_CELL).setCellStyle(secondSectionCellStyle);
        secondSection.getCell(CmsCustomValue.SECONDSECTION_CELL).setCellValue(topSectionInfo.get(2) + "대표님의 " +  topSectionInfo.get(4)+" 환급세액 청구서입니다");
    }
    private void detailResultRow(XSSFWorkbook xssfWorkbook,XSSFSheet sheet, List<List<String>> detailMonthlyResult, List<String> totalResult,boolean isPaging) {
        CellStyle detailResultRowCellStyle = cellStyleCustom(xssfWorkbook);
        if(isPaging){
            for(int i=0;i<detailMonthlyResult.size();i++) {
                // TODO: 2022/07/15 엑셀양식 15개 max 라서 건수 많으면 추가로 그릴 시트 요청 필요.
                XSSFRow detailResultRow = sheet.getRow(i + CmsCustomValue.DETAILRESULT_ROW_PAGING);
                detailResultRow.createCell(0, STRING);
                detailResultRow.getCell(0).setCellValue(i + 1);
                for (int j = CmsCustomValue.DETAILRESULT_STARTCELL; j <= CmsCustomValue.DETAILRESULT_ENDCELL; j += 2) {
                    if (j == 1) {
                        detailResultRow.createCell(j, STRING).setCellStyle(detailResultRowCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(1));//판매일자
                    } else if (j == 3) {
                        detailResultRow.createCell(j, STRING).setCellStyle(detailResultRowCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(0));//구매일련번호
                    } else if (j == 5) {
                        detailResultRow.createCell(j, STRING).setCellStyle(detailResultRowCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(3));//세금포함가격
                    } else {
                        detailResultRow.createCell(j, STRING).setCellStyle(detailResultRowCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(4));//부가가치세
                    }
                }
            }
        }
        else {
            for (int i = 0; i < detailMonthlyResult.size(); i++) {
                // TODO: 2022/07/15 엑셀양식 15개 max 라서 건수 많으면 추가로 그릴 시트 요청 필요.
                XSSFRow detailResultRow = sheet.getRow(i + CmsCustomValue.DETAILRESULT_ROW);
                detailResultRow.createCell(0, STRING);
                detailResultRow.getCell(0).setCellValue(i + 1);
                for (int j = CmsCustomValue.DETAILRESULT_STARTCELL; j <= CmsCustomValue.DETAILRESULT_ENDCELL; j += 2) {
                    if (j == 1) {
                        detailResultRow.createCell(j, STRING).setCellStyle(detailResultRowCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(1));//판매일자
                    } else if (j == 3) {
                        detailResultRow.createCell(j, STRING).setCellStyle(detailResultRowCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(0));//구매일련번호
                    } else if (j == 5) {
                        detailResultRow.createCell(j, STRING).setCellStyle(detailResultRowCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(3));//세금포함가격
                    } else {
                        detailResultRow.createCell(j, STRING).setCellStyle(detailResultRowCellStyle);
                        detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(4));//부가가치세
                    }
                }
                if (i == detailMonthlyResult.size() - 1) {
                    detailResultRow = sheet.getRow(45);
                    detailResultRow.createCell(5, STRING).setCellStyle(detailResultRowCellStyle);
                    detailResultRow.createCell(7, STRING).setCellStyle(detailResultRowCellStyle);
                    detailResultRow.getCell(5).setCellValue(totalResult.get(1));
                    detailResultRow.getCell(7).setCellValue(totalResult.get(2));
                }
            }
        }
    }
    private List<String> topSectionInfo(Long franchiseeIndex, String year, String month) {
        String nowMonth = String.valueOf(LocalDate.now().getMonthValue());
        if(nowMonth.length() == 1){
            nowMonth= "0" + nowMonth;
        }
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        List<String> result = new ArrayList<>();
        result.add("KTP 제 " + year + month); // 문서 번호
        result.add(year + nowMonth + "15");
        result.add(franchiseeEntity.getStoreName()); //수신
        result.add(franchiseeEntity.getStoreName() + month +"월 " + "내국세 환급세액 청구" ); // 제목
        result.add("[ "+ year + "년" + month + "월 01일 ~ " + month + "월 31일 ]" );
        return result;
    }


    public String cmsDownloadsTest(Long franchiseeIndex, String requestDate) {
        try {
            ClassPathResource resource = new ClassPathResource("KTP_CMS_Form.xlsx");
            File file1;
            try (InputStream inputStream = resource.getInputStream()) {
                file1 = File.createTempFile("KTP_CMS_Form", "xlsx");
                FileUtils.copyInputStreamToFile(inputStream, file1);
            }
            FileInputStream fileInputStream = new FileInputStream(file1);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFRow row = sheet.createRow(12);
            XSSFRow row1 = sheet.getRow(11);
            row.createCell(11, STRING);
            row.createCell(5, STRING);
            row1.getCell(5).setCellValue("수신자만 바뀌었으면 성공");
            row.getCell(5).setCellValue("기존것도 바뀌는지 테스트"); //실패
            row.getCell(11).setCellValue("가맹점번호 : " + franchiseeIndex + "가 입력되었습니다.");
            // TODO: 2022/02/03 엑셀파일 일부 write 후 저장까지 테스트 완료 포멧에 맞게 입력하는 로직 정해지면 구현할 것

            FileOutputStream fileOutputStream = new FileOutputStream("/Users/backend/Downloads/zipTest/excelExportTest.xlsx", false);
            xssfWorkbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            System.out.println("아웃풋스트림 생성 전");
            /*String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook);
            return result;*/

            return "test";


        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }

//    FranchiseeCmsResponseDetailInterface franchiseeCmsResponseDetailInterface = cmsDetail(franchiseeIndex, requestDate);
//    return "실패";
    }

    public String cmsDownloadsTest(Long franchiseeIndex, int i) {
        try {
            ClassPathResource resource = new ClassPathResource("KTP_CMS_Form.xlsx");
            File file1;
            try (InputStream inputStream = resource.getInputStream()) {
                file1 = File.createTempFile("KTP_CMS_Form", "xlsx");
                FileUtils.copyInputStreamToFile(inputStream, file1);
            }
            FileInputStream fileInputStream = new FileInputStream(file1);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFRow row = sheet.createRow(12);
            XSSFRow row1 = sheet.getRow(11);
            row.createCell(11, STRING);
            row.createCell(5, STRING);
            row1.getCell(5).setCellValue("수신자만 바뀌었으면 성공");
            row.getCell(5).setCellValue("기존것도 바뀌는지 테스트"); //실패
            row.getCell(11).setCellValue("가맹점번호 : " + franchiseeIndex + "가 입력되었습니다.");
            // TODO: 2022/02/03 엑셀파일 일부 write 후 저장까지 테스트 완료 포멧에 맞게 입력하는 로직 정해지면 구현할 것
            //String fileName = "/Users/backend/Downloads/zipTest/excelExportTest" +i +".xlsx";
            String fileName = "/home/success/downloads/excelExportTest" +i +".xlsx";
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
            xssfWorkbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            System.out.println("아웃풋스트림 생성 전");
            /*String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook);
            return result;*/

            return "test";


        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }

//    FranchiseeCmsResponseDetailInterface franchiseeCmsResponseDetailInterface = cmsDetail(franchiseeIndex, requestDate);
//    return "실패";
    }

    // 파일 여러개 압축
    public void zipFileDown(int i) throws IOException {

        final String folder = "/home/success/downloads/";
        List<File> files = new ArrayList<>();
        for(int j=0;j<i;j++){
            File file = new File(folder,"excelExportTest" + j + ".xlsx");
            files.add(file);
        }

        File zipFile = new File(folder,"zipTest.zip");
        byte[] buf = new byte[4096];

        try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))){
            for(File file : files){
                try(FileInputStream in = new FileInputStream(file)){
                    ZipEntry ze = new ZipEntry(file.getName());
                    out.putNextEntry(ze);

                    int len;
                    while((len = in.read(buf))>0){
                        out.write(buf,0,len);
                    }
                    out.closeEntry();
                }
            }
        }
        System.out.println("Zip Success");

    }

    private CellStyle cellStyleCustom(XSSFWorkbook xssfWorkbook){
        CellStyle cellStyle = xssfWorkbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }
}
