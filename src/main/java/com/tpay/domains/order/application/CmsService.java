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
            // TODO: 2022/07/08 CMS 청구내역 엑셀파일 양식 새롭게 받음.

            XSSFRow row25 = sheet.createRow(25); // 총 환급 건수
            row25.createCell(9,STRING);
            row25.getCell(9).setCellValue("총환급");

            XSSFRow row27 = sheet.createRow(27); // 즉시환급건수
            row27.createCell(9,STRING);
            row27.getCell(9).setCellValue("즉시환급건수");

            XSSFRow row30 = sheet.createRow(30); // 총 환급 건수
            row30.createCell(9,STRING);
            row30.getCell(9).setCellValue("총 청구 환급 세액"); // 총청구환급세액

            XSSFRow row32 = sheet.createRow(32); // 즉시환급 청구액
            row32.createCell(9,STRING);
            row32.getCell(9).setCellValue("즉시환급 청구액");

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
