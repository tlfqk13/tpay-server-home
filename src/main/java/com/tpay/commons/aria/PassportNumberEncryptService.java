package com.tpay.commons.aria;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 여권번호 암호화
 * 초기 세팅 후 변경이력 없음. 관세청 여권번호 암호화 로직변경시에만 변경될 것으로 판단됨
 */
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
