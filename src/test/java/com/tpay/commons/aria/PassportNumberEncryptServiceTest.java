package com.tpay.commons.aria;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PassportNumberEncryptServiceTest {

    PassportNumberEncryptService encryptService = new PassportNumberEncryptService();
    PassportNumberDecryptService decryptService = new PassportNumberDecryptService();

    @Test
    void passportEncryptionTest() {
        String passportNumber = "GC465195";
        String encryptPassportNumber = encryptService.encrypt(passportNumber);
        System.out.println("encryptPassportNumber = " + encryptPassportNumber);
        String decryptPassportNumber = decryptService.decrypt(encryptPassportNumber);
        System.out.println("decryptPassportNumber = " + decryptPassportNumber);
        assertThat(passportNumber).isEqualTo(decryptPassportNumber);
    }

    @Test
    void passportDecryptionTest() {
        String encryptPassportNumber = "B7242243FEF3C4744B9A828CFBEB85A7";
        String decryptPassportNumber = decryptService.decrypt(encryptPassportNumber);
        System.out.println("decryptPassportNumber = " + decryptPassportNumber);
    }
}