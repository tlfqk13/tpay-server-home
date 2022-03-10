package com.tpay.domains.franchisee.application;


import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.converter.NumberFormatConverter;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.dto.cms.FranchiseeCmsDetailResponse;
import com.tpay.domains.franchisee.application.dto.cms.FranchiseeCmsResponse;
import com.tpay.domains.franchisee.application.dto.cms.FranchiseeCmsResponseDetailInterface;
import com.tpay.domains.franchisee.application.dto.cms.FranchiseeCmsResponseInterface;
import com.tpay.domains.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Service
@RequiredArgsConstructor
public class FranchiseeCmsService {

  private final OrderRepository orderRepository;
  private final S3FileUploader s3FileUploader;
  private final NumberFormatConverter numberFormatConverter;

  public FranchiseeCmsResponse cmsReport(Long franchiseeIndex, String requestDate) {
    List<String> date = setUpDate(requestDate);
    String year = date.get(0);
    String month = date.get(1);
    FranchiseeCmsResponseInterface queryResult = orderRepository.findMonthlyCmsReport(franchiseeIndex, year, month);
    if(queryResult==null){
      return FranchiseeCmsResponse.builder().totalAmount("0").totalCount("0").totalVat("0").totalCommission("0").build();
    }

    return FranchiseeCmsResponse.builder()
        .totalCount(queryResult.getTotalCount())
        .totalAmount(queryResult.getTotalAmount())
        .totalCommission(queryResult.getTotalCommission())
        .totalVat(queryResult.getTotalVat())
        .build();

  }

  public FranchiseeCmsDetailResponse cmsDetail(Long franchiseeIndex, String requestDate) {
    List<String> date = setUpDate(requestDate);
    String year = date.get(0);
    String month = date.get(1);
    FranchiseeCmsResponseDetailInterface queryResult = orderRepository.findMonthlyCmsDetail(franchiseeIndex, year, month);
    if(queryResult==null){
      return FranchiseeCmsDetailResponse.builder()
          .commissionInfoList(Arrays.asList("0","0","0","0"))
          .customerInfoList(Arrays.asList("","","","","0")).build();
    }
    List<String> commissionInfoList = new ArrayList<>();
    List<String> customerInfoList = new ArrayList<>();
    commissionInfoList.add(numberFormatConverter.addCommaToNumber(queryResult.getTotalCount())+"건");
    commissionInfoList.add(numberFormatConverter.addCommaToNumber(queryResult.getTotalAmount()));
    commissionInfoList.add(numberFormatConverter.addCommaToNumber(queryResult.getTotalVat()));
    commissionInfoList.add(numberFormatConverter.addCommaToNumber(queryResult.getTotalCommission()));

    customerInfoList.add(queryResult.getSellerName());
    customerInfoList.add(queryResult.getBankName());
    customerInfoList.add(queryResult.getAccountNumber());
    customerInfoList.add(queryResult.getWithdrawalDate()+"일");
    customerInfoList.add(numberFormatConverter.addCommaToNumber(queryResult.getTotalBill()));
    return FranchiseeCmsDetailResponse.builder().commissionInfoList(commissionInfoList).customerInfoList(customerInfoList).build();
  }

  public String cmsDownloads(Long franchiseeIndex, String requestDate) {
    try {
      ClassPathResource resource = new ClassPathResource("KTP_CMS_Form.xlsx");
      File file = resource.getFile();
      FileInputStream fileInputStream = new FileInputStream(file);
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
//      FileOutputStream fileOutputStream = new FileOutputStream("/Users/sunba/excelExportTest.xlsx", false);
//      xssfWorkbook.write(fileOutputStream);
//      fileOutputStream.flush();
//      fileOutputStream.close();

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
    String requestDate = "20"+requestDatePart;
    List<String> dateList = new ArrayList<>();
    String year = requestDate.substring(0, 4);
    String month = requestDate.substring(4);
    int monthInt = Integer.parseInt(month);
    if (!(requestDate.length() == 6 && monthInt <= 12 && monthInt >= 1)) {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid Date format");
    }
    dateList.add(year);
    dateList.add(month);
    return dateList;
  }
}
