package com.tpay.domains.order.application;


import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.dto.CmsDetailDto;
import com.tpay.domains.order.application.dto.CmsDetailResponse;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.refund.application.RefundDetailFindService;
import com.tpay.domains.vat.application.dto.VatTotalDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
import java.util.Arrays;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Service
@RequiredArgsConstructor
@Slf4j
public class CmsService {

    private final OrderRepository orderRepository;
    private final S3FileUploader s3FileUploader;
    private final OrderService orderService;
    private final FranchiseeFindService franchiseeFindService;
    private final RefundDetailFindService refundDetailFindService;

    public VatTotalDto.Response cmsReport(Long franchiseeIndex, String requestDate) {
        List<LocalDate> date = setUpDate(requestDate);
        LocalDate startLocalDate = date.get(0);
        LocalDate endLocalDate = date.get(1);
        VatTotalDto.Response response = orderRepository.findMonthlyTotal(franchiseeIndex, startLocalDate, endLocalDate, RefundType.ALL);

        if (response == null) {
            return VatTotalDto.Response.builder().totalAmount("0").totalCount("0").totalVat("0").totalCommission("0").build();
        }

        return VatTotalDto.Response.builder()
                .totalCount(response.getTotalCount())
                .totalAmount(response.getTotalAmount())
                .totalRefund(response.getTotalRefund())
                .totalCommission(response.getTotalCommission())
                .totalVat(response.getTotalVat())
                .build();

    }

    public CmsDetailResponse cmsDetail(Long franchiseeIndex, String requestDate) {
        List<LocalDate> date = setUpDate(requestDate);
        LocalDate startLocalDate = date.get(0);
        LocalDate endLocalDate = date.get(1);

        VatTotalDto.Response vatTotalResponse = orderRepository.findMonthlyTotal(franchiseeIndex, startLocalDate, endLocalDate, RefundType.ALL);
        CmsDetailDto.Response cmsTotalResponse = orderRepository.findCmsBankInfo(franchiseeIndex);

        if (vatTotalResponse == null) {
            return CmsDetailResponse.builder()
                    .commissionInfoList(Arrays.asList("0", "0", "0", "0"))
                    .customerInfoList(Arrays.asList("", "", "", "", "0")).build();
        }
        List<String> commissionInfoList = new ArrayList<>();
        List<String> customerInfoList = new ArrayList<>();
        commissionInfoList.add(NumberFormatConverter.addCommaToNumber(vatTotalResponse.getTotalCount()) + "건");
        commissionInfoList.add(NumberFormatConverter.addCommaToNumber(vatTotalResponse.getTotalAmount()));
        commissionInfoList.add(NumberFormatConverter.addCommaToNumber(vatTotalResponse.getTotalVat()));
        commissionInfoList.add(NumberFormatConverter.addCommaToNumber(vatTotalResponse.getTotalCommission()));

        customerInfoList.add(cmsTotalResponse.getSellerName());
        customerInfoList.add(cmsTotalResponse.getBankName());
        customerInfoList.add(cmsTotalResponse.getAccountNumber());
        customerInfoList.add(cmsTotalResponse.getWithdrawalDate() + "일");
        customerInfoList.add(NumberFormatConverter.addCommaToNumber(vatTotalResponse.getTotalCommission()));

        String downloadLink = buildCmsFile(franchiseeIndex, requestDate, RefundType.ALL, vatTotalResponse);

        return CmsDetailResponse.builder()
                .commissionInfoList(commissionInfoList)
                .customerInfoList(customerInfoList)
                .downloadLink(downloadLink)
                .build();
    }

    private String buildCmsFile(Long franchiseeIndex, String requestDate, RefundType refundType) {
        return buildCmsFile(franchiseeIndex, requestDate, refundType, null);
    }

    private String buildCmsFile(Long franchiseeIndex, String requestDate, RefundType refundType, VatTotalDto.Response vatTotalResponse) {
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
            // 2022/02/03 엑셀파일 일부 write 후 저장까지 테스트 완료 포멧에 맞게 입력하는 로직 정해지면 구현할 것
            // 2022/07/14 CMS 청구내역 엑셀파일 양식 새롭게 받음.

            List<LocalDate> date = setUpDate(requestDate);
            LocalDate startLocalDate = date.get(0);
            LocalDate endLocalDate = date.get(1);

            // 1. 물품판매 상세내역
            boolean isPaging = true;
            boolean isCms = true; // cms 는 사후,즉시 구분이 없기때문에
            List<List<String>> detailMonthlyResult = orderService.findCmsVatDetail(franchiseeIndex, startLocalDate, endLocalDate, isPaging, refundType, isCms);
            // 2. 물품판매 총합계
            List<String> totalResult;
            if (null == vatTotalResponse) {
                totalResult = orderService.findCmsVatTotal(franchiseeIndex, startLocalDate, endLocalDate, refundType, isCms);
            } else {
                totalResult = vatTotalResponseToStringList(vatTotalResponse);
            }
            // TopSection
            List<String> topSectionInfo = this.topSectionInfo(franchiseeIndex, startLocalDate, endLocalDate);

            // 최상단 영역
            topSection(sheet, topSectionInfo);
            // [매장명] [날짜] 영역
            secondSection(xssfWorkbook, sheet, topSectionInfo);
            // 총 건수 . 청구 금액
            totalResultRow(sheet, totalResult);

            log.trace("detailMonthlyResult.size() : {} ", detailMonthlyResult.size());
            log.trace("franchiseeIndex : {} ", franchiseeIndex);

            detailResultRow(xssfWorkbook, sheet, detailMonthlyResult, totalResult, false);
            if (detailMonthlyResult.size() >= 15) {
                // 물품상세 내역
                detailResultRow(xssfWorkbook, sheet1, detailMonthlyResult, totalResult, isPaging);
            }

            StringBuilder fileName = new StringBuilder();
            fileName.append(topSectionInfo.get(2)).append("_").append(startLocalDate.getMonthValue()).append("월").append("_cms");
            String result = s3FileUploader.uploadXlsx(franchiseeIndex, xssfWorkbook, fileName, String.valueOf(startLocalDate.getMonthValue()), true);
            return result;
        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }
    }

    private List<String> vatTotalResponseToStringList(VatTotalDto.Response response) {
        return Arrays.asList(
                NumberFormatConverter.addCommaToNumber(response.getTotalCount())
                , NumberFormatConverter.addCommaToNumber(response.getTotalAmount())
                , NumberFormatConverter.addCommaToNumber(response.getTotalVat())
                , NumberFormatConverter.addCommaToNumber(response.getTotalRefund())
                , NumberFormatConverter.addCommaToNumber(response.getTotalCommission())
        );
    }

    public void cmsAdminDownloads(String requestDate, RefundType refundType) {
        List<LocalDate> date = setUpDate(requestDate);
        LocalDate startLocalDate = date.get(0);
        LocalDate endLocalDate = date.get(1);

        String requestYearMonthly = String.valueOf(endLocalDate.getYear()).substring(2)
                + endLocalDate.getMonthValue();
        log.trace(" @@ requestYearMonthly = {}", requestYearMonthly);

        List<List<String>> totalResult = refundDetailFindService.findFranchiseeId(startLocalDate, endLocalDate);
        for (List<String> strings : totalResult) {
            this.buildCmsFile(Long.valueOf(strings.get(0)), requestYearMonthly, refundType);
        }

    }

    private void topSection(XSSFSheet sheet, List<String> topSectionInfo) {
        for (int i = CmsCustomValue.TOPSECTION_STARTROW; i <= CmsCustomValue.TOPSECTION_ENDROW; i += 2) {
            XSSFRow topSection = sheet.getRow(i);
            if (i == 8) {
                topSection.createCell(CmsCustomValue.TOPSECTION_FIRSTCELL);
                topSection.createCell(CmsCustomValue.TOPSECTION_SECONDCELL);
                topSection.getCell(CmsCustomValue.TOPSECTION_FIRSTCELL).setCellValue(topSectionInfo.get(0));
                topSection.getCell(CmsCustomValue.TOPSECTION_SECONDCELL).setCellValue(topSectionInfo.get(1));
            } else if (i == 10) {
                topSection.createCell(CmsCustomValue.TOPSECTION_FIRSTCELL);
                topSection.getCell(CmsCustomValue.TOPSECTION_FIRSTCELL).setCellValue(topSectionInfo.get(2));
            } else if (i == 12) {
                topSection.createCell(CmsCustomValue.TOPSECTION_FIRSTCELL);
                topSection.getCell(CmsCustomValue.TOPSECTION_FIRSTCELL).setCellValue("참조");
            } else {
                topSection.createCell(CmsCustomValue.TOPSECTION_FIRSTCELL);
                topSection.getCell(CmsCustomValue.TOPSECTION_FIRSTCELL).setCellValue(topSectionInfo.get(3));
            }
        }
    }

    private void totalResultRow(XSSFSheet sheet, List<String> totalResult) {
        XSSFRow totalResultRow = sheet.getRow(CmsCustomValue.TOTALRESULT_ROW);
        totalResultRow.createCell(CmsCustomValue.TOTALRESULT_FIRSTCELL);
        totalResultRow.createCell(CmsCustomValue.TOTALRESULT_SECONDCELL);
        totalResultRow.getCell(CmsCustomValue.TOTALRESULT_FIRSTCELL).setCellValue(totalResult.get(0));
        totalResultRow.getCell(CmsCustomValue.TOTALRESULT_SECONDCELL).setCellValue(totalResult.get(4)); // 청구내역
    }

    private void secondSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, List<String> topSectionInfo) {
        XSSFRow secondSection = sheet.getRow(CmsCustomValue.SECONDSECTION_ROW);
        CellStyle secondSectionCellStyle = secondSectionCellStyle(xssfWorkbook);
        secondSection.createCell(CmsCustomValue.SECONDSECTION_CELL).setCellStyle(secondSectionCellStyle);
        secondSection.getCell(CmsCustomValue.SECONDSECTION_CELL).setCellValue(topSectionInfo.get(2) + "대표님의 " + topSectionInfo.get(4) + " 환급세액 청구서입니다");
    }

    private void detailResultRow(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, List<List<String>> detailMonthlyResult, List<String> totalResult, boolean isPaging) {
        CellStyle detailResultRowCellStyle = cellStyleCustom(xssfWorkbook);
        if (isPaging) {
            for (int i = 0; i < detailMonthlyResult.size(); i++) {
                // 2022/07/15 엑셀양식 15개 max 라서 건수 많으면 추가로 그릴 시트 요청 필요.
                XSSFRow detailResultRow = sheet.getRow(i + CmsCustomValue.DETAILRESULT_ROW_PAGING);
                detailResultRow.createCell(0, STRING).setCellStyle(detailResultRowCellStyle);
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
        } else {
            for (int i = 0; i < detailMonthlyResult.size(); i++) {
                // 2022/07/15 엑셀양식 15개 max 라서 건수 많으면 추가로 그릴 시트 요청 필요.
                XSSFRow detailResultRow = sheet.getRow(i + CmsCustomValue.DETAILRESULT_ROW);
                detailResultRow.createCell(0, STRING).setCellStyle(detailResultRowCellStyle);
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
                    detailResultRow = sheet.getRow(74);
                    detailResultRow.createCell(5, STRING).setCellStyle(detailResultRowCellStyle);
                    detailResultRow.createCell(7, STRING).setCellStyle(detailResultRowCellStyle);
                    detailResultRow.getCell(5).setCellValue(totalResult.get(1));
                    detailResultRow.getCell(7).setCellValue(totalResult.get(2));
                }
            }
        }
    }

    private List<String> topSectionInfo(Long franchiseeIndex, LocalDate startLocalDate, LocalDate endLocalDate) {

        String year = String.valueOf(startLocalDate.getYear());
        String month = String.valueOf(endLocalDate.getMonthValue());
        String nowMonth = String.valueOf(LocalDate.now().getMonthValue());

        if (nowMonth.length() == 1) {
            nowMonth = "0" + nowMonth;
        }

        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        List<String> result = new ArrayList<>();
        result.add("KTP 제 " + year + month); // 문서 번호
        result.add(year + nowMonth + "05"); // 발송일자
        result.add(franchiseeEntity.getStoreName()); //수신
        result.add(franchiseeEntity.getStoreName() + month + "월 " + "내국세 환급세액 청구"); // 제목
        result.add("[ " + year + "년" + month + "월 01일 ~ " + month + "월 31일 ]");
        return result;
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

        log.trace(" @@ startDate = {}", startDate);
        log.trace(" @@ endDate = {}", endDate);

        dateList.add(startDate);
        dateList.add(endDate);
        return dateList;
    }

    private CellStyle cellStyleCustom(XSSFWorkbook xssfWorkbook) {
        CellStyle cellStyle = xssfWorkbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    private CellStyle secondSectionCellStyle(XSSFWorkbook xssfWorkbook) {
        CellStyle cellStyle = xssfWorkbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

}
