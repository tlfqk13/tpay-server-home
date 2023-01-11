package com.tpay.domains.refund.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
import java.util.List;

import static com.tpay.domains.refund.application.RefundReceiptCustomValue.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class RefundReceiptDownloadsService {
    private final RefundReceiptFindService refundReceiptFindService;
    public void downloadsRefundReceipt(RefundReceiptDto.Request request) {

        try {
            List<RefundReceiptDto.Response> response = refundReceiptFindService.downloadsRefundReceiptDetail(request);
            String passportNumber = request.getPassportNumber();
            for (RefundReceiptDto.Response value : response) {

                ClassPathResource resource;
                if(value.isRefundAfter()) {
                    resource = new ClassPathResource(REFUND_AFTER_RECEIPT);
                    log.trace(" @@ resource.getPath = {}", resource.getPath());
                    log.trace(" @@ resource.getURL = {}", resource.getURL());
                    log.trace(" @@ resource.getFilename = {}", resource.getFilename());
                }else{
                    resource = new ClassPathResource(REFUND_IMMEDIATE_RECEIPT);
                }
                File file;
                try (InputStream inputStream = resource.getInputStream()) {
                    file = File.createTempFile("RefundReceipt", "xlsx");
                    FileUtils.copyInputStreamToFile(inputStream, file);
                }

                FileInputStream fileInputStream = new FileInputStream(file);
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
                XSSFSheet sheet = xssfWorkbook.getSheetAt(0); // 첫번째 시트를 가져옴
                String fileName = "Receipt " + value.getSaleDate().substring(0, 18) + ".xlsx";
                refundReceiptResultSection(xssfWorkbook, sheet, value, passportNumber, value.isRefundAfter(), fileName);
            }
        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void refundReceiptResultSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet
            , RefundReceiptDto.Response response, String passportNumber, boolean isRefundAfter,String fileName) throws IOException {

        CellStyle costCellStyle = costCellStyleCustom(xssfWorkbook);

        // 즉시환급(1단계)
        resultSection(sheet, response, passportNumber, costCellStyle);

        // 사후환급(2,3단계)
        resultSection(sheet, response, passportNumber, isRefundAfter, costCellStyle);

    }

    private void resultSection(XSSFSheet sheet, RefundReceiptDto.Response response, String passportNumber, boolean isRefundAfter, CellStyle costCellStyle) {
        XSSFRow resultSectionRow;
        if(isRefundAfter) {
            XSSFRow refundAfterSection = sheet.getRow(21);
            refundAfterSection.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalAmount()));
            refundAfterSection.createCell(COLUMN_INDEX).setCellStyle(costCellStyle);
            refundAfterSection.createCell(COLUMN_INDEX-1).setCellStyle(costCellStyle);

            refundAfterSection= sheet.getRow(22);
            refundAfterSection.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalVat())); //부가가치세

            resultSectionRow= sheet.getRow(23);
            resultSectionRow.getCell(COLUMN_INDEX).setCellValue("0"); // 개별소비세

            refundAfterSection = sheet.getRow(27); // 새엑계
            refundAfterSection.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalRefund())); // 세엑계

            refundAfterSection = sheet.getRow(28);
            refundAfterSection.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(response.getCategory())); //제비용

            refundAfterSection = sheet.getRow(29);
            refundAfterSection.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalRefund())); // 환급액

            refundAfterSection = sheet.getRow(30);
            refundAfterSection.createCell(COLUMN_INDEX).setCellValue(response.getExpireDate()); // 반출유효기간

            refundAfterSection = sheet.getRow(33);
            refundAfterSection.createCell(COLUMN_INDEX).setCellValue(passportNumber);

            refundAfterSection = sheet.getRow(42);
            refundAfterSection.createCell(COLUMN_INDEX-1).setCellValue("( " + response.getPurchaseSn() + " )");
        }
    }

    private void resultSection(XSSFSheet sheet, RefundReceiptDto.Response response, String passportNumber, CellStyle costCellStyle) {

        XSSFRow LogoSectionRow = sheet.getRow(LOGO_SECTION_ROW);
        LogoSectionRow.createCell(COLUMN_INDEX-1).setCellValue("( " + response.getPurchaseSn() + " )");

        XSSFRow resultSectionRow = sheet.getRow(RESULT_SECTION_ROW);
        resultSectionRow.createCell(COLUMN_INDEX).setCellValue(response.getTaxFreeStoreNumber());

        resultSectionRow= sheet.getRow(8);
        resultSectionRow.createCell(COLUMN_INDEX).setCellValue(response.getSaleDate());

        resultSectionRow= sheet.getRow(9);
        resultSectionRow.createCell(COLUMN_INDEX).setCellValue(response.getSellerName());

        resultSectionRow= sheet.getRow(10);
        resultSectionRow.createCell(COLUMN_INDEX).setCellValue(response.getFranchiseeName());

        resultSectionRow= sheet.getRow(11);
        resultSectionRow.createCell(COLUMN_INDEX).setCellValue(response.getBusinessNumber());

        resultSectionRow= sheet.getRow(13);
        resultSectionRow.createCell(COLUMN_INDEX -2).setCellValue(response.getStoreAddress());

        resultSectionRow= sheet.getRow(14);
        resultSectionRow.createCell(COLUMN_INDEX-2).setCellValue(response.getSellerName());
        resultSectionRow.createCell(COLUMN_INDEX-1).setCellValue(response.getStoreTelNumber());

        resultSectionRow.createCell(COLUMN_INDEX).setCellStyle(costCellStyle);

        resultSectionRow= sheet.getRow(17); // 단가
        resultSectionRow.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(String.valueOf(Integer.parseInt(response.getTotalAmount()) - Integer.parseInt(response.getTotalVat()))));

        resultSectionRow= sheet.getRow(19); // 금액
        resultSectionRow.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalAmount()));

        resultSectionRow= sheet.getRow(20); // 판매총액
        resultSectionRow.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalAmount()));

        resultSectionRow= sheet.getRow(21); // 부가가치세
        resultSectionRow.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalVat()));

        resultSectionRow= sheet.getRow(22); // 즉시환급상당액
        resultSectionRow.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalRefund()));

        resultSectionRow= sheet.getRow(23); // 결제금액
        resultSectionRow.getCell(COLUMN_INDEX).setCellValue(NumberFormatConverter.addCommaToNumber(String.valueOf(Integer.parseInt(response.getTotalAmount()) - Integer.parseInt(response.getTotalRefund()))));

        resultSectionRow= sheet.getRow(26);
        resultSectionRow.createCell(COLUMN_INDEX).setCellValue(passportNumber);
    }

    private CellStyle costCellStyleCustom(XSSFWorkbook xssfWorkbook){
        CellStyle personalInfoResultCellStyle = xssfWorkbook.createCellStyle();
        personalInfoResultCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        return personalInfoResultCellStyle;
    }
}
