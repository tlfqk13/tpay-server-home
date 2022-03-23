package com.tpay.domains.barcode.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class BarcodeCreateService {

  private final WebClient webClient;

  public BarcodeCreateResponse createBarCode(RefundLimitRequest request){
    WebClient.Builder
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
