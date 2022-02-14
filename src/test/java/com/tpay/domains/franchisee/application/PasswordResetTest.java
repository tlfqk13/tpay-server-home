package com.tpay.domains.franchisee.application;


import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PasswordResetTest {
  @Autowired
  FranchiseeRepository franchiseeRepository;
  @Autowired
  PasswordResetService passwordResetService;
  FranchiseeEntity franchiseeEntity;
  Long franchiseeId;
  String businessNumber;


  @BeforeEach
  void setup() {
    franchiseeEntity =
        FranchiseeEntity.builder()
            .businessNumber("123-33-12345")
            .storeName("SuccessMode")
            .storeAddressNumber("00011")
            .storeAddressBasic("안양시")
            .storeAddressDetail("평촌동")
            .sellerName("Kim")
            .storeTel("010-1234-1234")
            .productCategory("잡화")
            .password("qq123456!!")
            .signboard("간판")
            .storeNumber("031-234-2345")
            .email("abc@defg.co.kr")
            .isTaxRefundShop("false")
            .build();
    FranchiseeEntity save = franchiseeRepository.save(franchiseeEntity);
    franchiseeId = save.getId();
    businessNumber = save.getBusinessNumber();
  }

  @Test
  public void 사업자번호_존재여부_성공() {
    //when
    boolean result = passwordResetService.existBusinessNumber(businessNumber);
    //then
    assertThat(result).isEqualTo(true);
  }

  @Test
  public void 사업자번호_존재여부_실패() {
    //when
    boolean result = passwordResetService.existBusinessNumber(businessNumber + 1);
    //then
    assertThat(result).isEqualTo(false);
  }


}
