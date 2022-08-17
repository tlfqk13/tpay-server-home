package com.tpay.commons.aria;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        String encryptPassportNumber = "34F88CCCB3BCE4014CFF56764447DEFD";
        String decryptPassportNumber = decryptService.decrypt(encryptPassportNumber);
        System.out.println("decryptPassportNumber = " + decryptPassportNumber);
    }
}