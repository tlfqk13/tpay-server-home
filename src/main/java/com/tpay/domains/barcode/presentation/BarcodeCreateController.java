package com.tpay.domains.barcode.presentation;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.domains.barcode.application.dto.BarcodeCreateRequest;
import lombok.RequiredArgsConstructor;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class BarcodeCreateController {

  @PostMapping("/barcode")
  public void barcodeMaker(
      @RequestBody BarcodeCreateRequest barcodeCreateRequest
  ) throws Exception {
    // TODO: 2022/03/03   여권번호 길이는 고정값으로하면 좋음(바코드 인식 등 오류 최소화)
    // 그러므로 최대 여권번호 길이 기반으로 하면 될 듯
    String passportNumberWithSpaces = setPassportNumberWithSpaces(barcodeCreateRequest.getPassportNumber());
    String deduction = barcodeCreateRequest.getDeduction();
    Barcode barcode = BarcodeFactory.createCode128(passportNumberWithSpaces + deduction);
    File file = new File("/Users/sunba/Desktop/testPNG.png");
    BarcodeImageHandler.savePNG(barcode, file);
  }


  static String setPassportNumberWithSpaces(String passportNumber) {
    StringBuilder stringBuilder = new StringBuilder();
    StringBuilder passportNumberTen = stringBuilder.append(passportNumber);
    if (passportNumberTen.length() <= 10) {
      while (passportNumberTen.length() < 10) {
        passportNumberTen.append(" ");
      }
    } else {
      throw new InvalidPassportInfoException(ExceptionState.INVALID_PASSWORD, "PassportNumber too Long (Max : 10)");
    }
    return passportNumberTen.toString();
  }
}
