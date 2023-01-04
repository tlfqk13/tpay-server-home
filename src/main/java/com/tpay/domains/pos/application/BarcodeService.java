package com.tpay.domains.pos.application;

import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BarcodeService {

    private final S3FileUploader s3FileUploader;

    public String createBarcode(Long id, String deduction) {

        String deductionPadding = deduction.substring(3);
        String idPadding = setWithZero(id.toString(), 7);

        String uri = "";

        try {
            //바코드 생성
            Barcode barcode = BarcodeFactory.createCode128A(idPadding + deductionPadding);
            barcode.setBarHeight(100);
            BufferedImage image = BarcodeImageHandler.getImage(barcode);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", os);

            //Input stream 생성
            InputStream is = new ByteArrayInputStream(os.toByteArray());

            //S3 업로드
            uri = s3FileUploader.uploadBarcode(id, is);
        } catch (OutputException | BarcodeException | IOException e) {
            log.error("Barcode Create Error : {}", e.getMessage());
        }
        return uri;
    }

    static String setWithZero(String target, Integer size) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder after = stringBuilder.append(target);
        if (after.length() <= size) {
            while (after.length() < size) {
                after.insert(0, "0");
            }
        } else {
            throw new InvalidPassportInfoException(ExceptionState.INVALID_PASSWORD, "too long target to padding(target > size)");
        }
        return after.toString();
    }

    // 구매일련번호 바코드 생성 - ktaxpay 영수증용
    public String createBarcode(String orderNumber,Long orderId) {
        String uri = "";

        try {
            //바코드 생성
            Barcode barcode = BarcodeFactory.createCode128A(orderNumber);
            barcode.setBarHeight(100);
            BufferedImage image = BarcodeImageHandler.getImage(barcode);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", os);

            //Input stream 생성
            InputStream is = new ByteArrayInputStream(os.toByteArray());

            //S3 업로드
            uri = s3FileUploader.uploadBarcode(orderId, is);
        } catch (OutputException | BarcodeException | IOException e) {
            e.printStackTrace();
            log.error("Barcode Create Error : {}", e.getMessage());
        }
        return uri;
    }
}
