package com.tpay.commons.aria;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import java.security.InvalidKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassportNumberDecryptService {
  public String decrypt(String passportNumber) {
    try {
      return ARIAEngine.decrypt(passportNumber);

    } catch(InvalidKeyException exception) {
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Decryption Failed");
    }
  }
}
