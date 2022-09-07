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
        String encryptPassportNumber = "4A9A739C81906A536C4ABEC2A1C8EE64";
        String decryptPassportNumber = decryptService.decrypt(encryptPassportNumber);
        System.out.println("decryptPassportNumber = " + decryptPassportNumber);
    }
}