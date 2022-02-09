package com.tpay.domains.refund_core.application;


import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class LimitFindServiceTest {

  @Autowired
  private CustomerFindService customerFindService;

  @Autowired
  private PassportNumberEncryptService passportNumberEncryptService;

  @BeforeEach
  public void setUp() {

  }


  @Test
  public void 국가_여권번호_조회테스트_조회성공() {
    RefundLimitRequest request = RefundLimitRequest.builder()
        .name("홍길동")
        .passportNumber("SUCCESS21")
        .nationality("GBR")
        .totalAmount("0")
        .saleDate(LocalDate.now().toString())
        .build();

    CustomerEntity customerEntity = customerFindService.findByNationAndPassportNumber(request.getName(), request.getPassportNumber(), request.getNationality());
    Assertions.assertThat(customerEntity.getPassportNumber()).isEqualTo(passportNumberEncryptService.encrypt(request.getPassportNumber()));

  }

  @Test
  public void 국가_여권번호_조회테스트_조회실패() {
    RefundLimitRequest request = RefundLimitRequest.builder()
        .name("홍길동")
        .passportNumber("SUCCESS21")
        .nationality("GBR")
        .totalAmount("0")
        .saleDate(LocalDate.now().toString())
        .build();

    RefundLimitRequest request2 = RefundLimitRequest.builder()
        .name(" 홍 길 동 ")
        .passportNumber("SUCCESS21")
        .nationality("KOR")
        .totalAmount("0")
        .saleDate(LocalDate.now().toString())
        .build();

    CustomerEntity customerEntity = customerFindService.findByNationAndPassportNumber(request.getName(), request.getPassportNumber(), request.getNationality());
    assertThrows(InvalidParameterException.class,() -> customerFindService.findByNationAndPassportNumber(request2.getName(), request2.getPassportNumber(), request2.getNationality()));
  }
}
