package com.tpay.commons.aria;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassportNumberEncryptService {
  public String encrypt(String passportNumber) {
    try {
      return ARIAEngine.encrypt(passportNumber);

    } catch (Exception exception) {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Decryption Failed");
    }
  }
}
