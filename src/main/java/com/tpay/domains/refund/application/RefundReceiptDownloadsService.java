package com.tpay.domains.refund.application;

import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.converter.NumberFormatConverter;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
@RequiredArgsConstructor
@Service
@Slf4j
public class RefundReceiptDownloadsService {
    private final RefundReceiptFindService refundReceiptFindService;
    private final S3FileUploader s3FileUploader;
    public void downloadsRefundReceipt(RefundReceiptDto.Request request) {
        try {
            ClassPathResource resource = new ClassPathResource("");
            if(request.isRefundAfter()) {
                log.trace(" @@  AfterRefundReceipt");
                resource = new ClassPathResource("AfterRefundReceipt.xlsx");
            }else{
                resource = new ClassPathResource("ImmediateRefundReceipt.xlsx");
            }
            File file;
            try (InputStream inputStream = resource.getInputStream()) {
                file = File.createTempFile("RefundReceipt", "xlsx");
                FileUtils.copyInputStreamToFile(inputStream, file);
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0); // 첫번째 시트를 가져옴

            List<RefundReceiptDto.Response> response = refundReceiptFindService.downloadsRefundReceiptDetail(request);
            String passportNumber = request.getPassportNumber();

            for(int i=0;i<response.size();i++) {
                String fileName = "refundReceipt_" + i +".xlsx";
                log.trace(" @@ request.isRefundAfter() = {}" + i , request.isRefundAfter());
                refundReceiptResultSection(xssfWorkbook, sheet, response.get(i), passportNumber, request.isRefundAfter(), fileName);
            }
        } catch (IOException e) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "File Input Failed");
        }
    }

    private void refundReceiptResultSection(XSSFWorkbook xssfWorkbook, XSSFSheet sheet
            , RefundReceiptDto.Response response, String passportNumber, boolean isRefundAfter,String fileName) throws IOException {

        XSSFRow LogoSectionRow = sheet.getRow(2);
        LogoSectionRow.createCell(11).setCellValue(response.getPurchaseSn());

        XSSFRow resultSectionRow = sheet.getRow(7);
        resultSectionRow.createCell(12).setCellValue(response.getTaxFreeStoreNumber());

        resultSectionRow=sheet.getRow(8);
        resultSectionRow.createCell(12).setCellValue(response.getSaleDate());

        resultSectionRow=sheet.getRow(9);
        resultSectionRow.createCell(12).setCellValue(response.getSellerName());

        resultSectionRow=sheet.getRow(10);
        resultSectionRow.createCell(12).setCellValue(response.getFranchiseeName());

        resultSectionRow=sheet.getRow(12);
        resultSectionRow.createCell(12).setCellValue(response.getBusinessNumber());

        resultSectionRow=sheet.getRow(12);
        resultSectionRow.createCell(12).setCellValue(response.getStoreAddress());
        resultSectionRow=sheet.getRow(13);
        resultSectionRow.createCell(10).setCellValue(response.getStoreAddress());

        resultSectionRow=sheet.getRow(14);
        resultSectionRow.createCell(12).setCellValue(response.getSellerName());

        resultSectionRow=sheet.getRow(17);
        resultSectionRow.getCell(12).setCellValue(NumberFormatConverter.addCommaToNumber(String.valueOf(Integer.parseInt(response.getTotalAmount()) - Integer.parseInt(response.getTotalVat()))));

        resultSectionRow=sheet.getRow(18);
        resultSectionRow.createCell(12).setCellValue("1");

        resultSectionRow=sheet.getRow(19);
        resultSectionRow.getCell(12).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalAmount()));

        resultSectionRow=sheet.getRow(20);
        resultSectionRow.getCell(12).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalAmount()));

        resultSectionRow=sheet.getRow(21);
        resultSectionRow.getCell(12).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalVat()));

        resultSectionRow=sheet.getRow(22);
        resultSectionRow.getCell(12).setCellValue(NumberFormatConverter.addCommaToNumber(response.getTotalRefund()));

        resultSectionRow=sheet.getRow(23);
        resultSectionRow.getCell(12).setCellValue(NumberFormatConverter.addCommaToNumber(String.valueOf(Integer.parseInt(response.getTotalAmount()) - Integer.parseInt(response.getTotalRefund()))));

        resultSectionRow=sheet.getRow(26);
        resultSectionRow.createCell(12).setCellValue(passportNumber);

        if(isRefundAfter) {
            XSSFRow refundAfterSection = sheet.getRow(22);
            refundAfterSection.createCell(12).setCellValue("부가가치세_");

            refundAfterSection = sheet.getRow(23);
            refundAfterSection.createCell(12).setCellValue("0");

            refundAfterSection = sheet.getRow(24);
            refundAfterSection.createCell(12).setCellValue("0");

            refundAfterSection = sheet.getRow(25);
            refundAfterSection.createCell(29).setCellValue("0");

            refundAfterSection = sheet.getRow(26);
            refundAfterSection.createCell(12).setCellValue("세엑계_");

            refundAfterSection = sheet.getRow(27);
            refundAfterSection.createCell(12).setCellValue("제비용_");

            refundAfterSection = sheet.getRow(28);
            refundAfterSection.createCell(12).setCellValue("환급액_");

            refundAfterSection = sheet.getRow(29);
            refundAfterSection.createCell(12).setCellValue("반출유효기간_");

            refundAfterSection = sheet.getRow(32);
            refundAfterSection.createCell(12).setCellValue(passportNumber);
        }
/*        // 로컬 저장
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/backend/" + fileName + ".xlsx", false);
        xssfWorkbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();*/

        String result = s3FileUploader.uploadRefundReceipt(xssfWorkbook,fileName);
    }
}
