package com.tpay.domains.order.application;


import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.order.application.dto.CmsDetailResponse;
import com.tpay.domains.order.application.dto.CmsResponse;
import com.tpay.domains.order.application.dto.CmsResponseDetailInterface;
import com.tpay.domains.order.application.dto.CmsResponseInterface;
import com.tpay.domains.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
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

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Service
@RequiredArgsConstructor
public class CmsService {

    private final OrderRepository orderRepository;
    private final S3FileUploader s3FileUploader;
    private final OrderService orderService;

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

    public String cmsDownloads(Long franchiseeIndex, String requestDate) {
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
            XSSFRow row1 = sheet.getRow(11);
            row1.getCell(5).setCellValue("수신자만 바뀌었으면 성공");
            // TODO: 2022/02/03 엑셀파일 일부 write 후 저장까지 테스트 완료 포멧에 맞게 입력하는 로직 정해지면 구현할 것
            // TODO: 2022/07/14 CMS 청구내역 엑셀파일 양식 새롭게 받음.

            List<String> date = this.setUpDate(requestDate);
            String year = date.get(0);
            String month = date.get(1);
            // 1. 물품판매 상세내역
            List<List<String>> detailMonthlyResult = orderService.findMonthlyDetail(franchiseeIndex, year, month);

            //2. 물품판매 총합계
            List<String> totalResult = orderService.findMonthlyTotal(franchiseeIndex, year, month);

            // 최상단 영역
            for(int i=8; i<=14; i+=2){
                XSSFRow topSection = sheet.getRow(i);
                if(i == 8){
                    topSection.createCell(1);
                    topSection.createCell(7);
                    topSection.getCell(1).setCellValue("문서번호");
                    topSection.getCell(7).setCellValue("발송일자");
                }else if(i == 10){
                    topSection.createCell(1);
                    topSection.getCell(1).setCellValue("수신");
                }else if(i == 12){
                    topSection.createCell(1);
                    topSection.getCell(1).setCellValue("참조");
                }else{
                    topSection.createCell(1);
                    topSection.getCell(1).setCellValue("제목입니다");
                }
            }

            // [매장명] [날짜] 영역
            XSSFRow secondSection = sheet.getRow(22);
            secondSection.createCell(0);
            secondSection.getCell(0).setCellValue("[매장명!!!] 대표님의 [2022년07월15일!!!] 환급세액 청구서입니다");

            // 총 건수 . 청구 금액
            XSSFRow totalResultRow = sheet.getRow(24);
            totalResultRow.createCell(3);
            totalResultRow.createCell(7);
            totalResultRow.getCell(3).setCellValue(totalResult.get(0));
            totalResultRow.getCell(7).setCellValue(totalResult.get(3));

            for(int i=0;i<detailMonthlyResult.size() + 1;i++){
                XSSFRow detailResultRow = sheet.getRow(i+30);
                detailResultRow.getCell(0).setCellValue(i+1);
                for(int j=1; j <= 9; j+=2){
                    detailResultRow.createCell(j,STRING);
                    detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(1));//판매일자
                    detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(0));//구매일련번호
                    detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(3));//세금포함각격
                    detailResultRow.getCell(j).setCellValue(detailMonthlyResult.get(i).get(4));//부가가치세
                }
                if(i == detailMonthlyResult.size()){
                    detailResultRow.getCell(5).setCellValue(totalResult.get(1));
                    detailResultRow.getCell(7).setCellValue(totalResult.get(2));
                }
            }

            //FileOutputStream fileOutputStream = new FileOutputStream("/Users/backend/Downloads/cmsMonthlyReport.xlsx", false);
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
}
