package com.tpay.domains.refund_core.application;


import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@ActiveProfiles(profiles = {"operation"})
public class LimitFindServiceTest {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private PassportNumberEncryptService passportNumberEncryptService;

  @Autowired
  private LimitFindService limitFindService;

  @BeforeEach
  public void setUp() {

  }


  @Test
  public void 국가_여권번호_조회테스트_조회성공() {
    RefundLimitRequest request = RefundLimitRequest.builder()
        .name("홍길동")
        .passportNumber("SUCCESS100")
        .nationality("GBR")
        .totalAmount("0")
        .saleDate(LocalDate.now().toString())
        .build();

      RefundResponse refundResponse = limitFindService.find(request);
      Assertions.assertThat(refundResponse.getResponseCode()).isEqualTo("0000");

  }

//  @Test
//  public void 국가_여권번호_조회테스트_조회실패() {
//    RefundLimitRequest request = RefundLimitRequest.builder()
//        .name("홍길동")
//        .passportNumber("SUCCESS21")
//        .nationality("GBR")
//        .totalAmount("0")
//        .saleDate(LocalDate.now().toString())
//        .build();
//
//    RefundLimitRequest request2 = RefundLimitRequest.builder()
//        .name(" 홍 길 동 ")
//        .passportNumber("SUCCESS21")
//        .nationality("KOR")
//        .totalAmount("0")
//        .saleDate(LocalDate.now().toString())
//        .build();
//
//    customerFindService.findByNationAndPassportNumber(request.getName(), request.getPassportNumber(), request.getNationality());
//    assertThrows(InvalidPassportInfoException.class, () -> customerFindService.findByNationAndPassportNumber(request2.getName(), request2.getPassportNumber(), request2.getNationality()));
//  }
}
