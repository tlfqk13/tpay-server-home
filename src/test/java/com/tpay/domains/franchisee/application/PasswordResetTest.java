package com.tpay.domains.franchisee.application;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.dto.PasswordChangeRequest;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
public class PasswordResetTest {
  @Autowired
  FranchiseeRepository franchiseeRepository;
  @Autowired
  PasswordResetService passwordResetService;
  FranchiseeEntity franchiseeEntity;
  Long franchiseeId;
  String businessNumber;
  FranchiseeEntity savedFranchiseeEntity;


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
            .storeTel("01012341234")
            .productCategory("잡화")
            .password("qq123456!!")
            .signboard("간판")
            .storeNumber("031-234-2345")
            .email("abc@defg.co.kr")
            .isTaxRefundShop("false")
            .build();
    FranchiseeEntity save = franchiseeRepository.save(franchiseeEntity);
    savedFranchiseeEntity = save;
    franchiseeId = save.getId();
    businessNumber = save.getBusinessNumber();
  }

  @AfterEach
  void setDelete() {
    franchiseeRepository.deleteAll();
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
    Throwable throwable = catchThrowable(() -> passwordResetService.existBusinessNumber(businessNumber + 1));
    //then
    assertThat(throwable)
        .isInstanceOf(InvalidParameterException.class);
  }

  @Test
  public void 본인인증_성공() {
    //given
    String businessNumber = "1-23331234-5"; //이상하게 줘도 되어야 함
    String name = "Kim";
    String phoneNumber = "010-123412-34"; //이상해게 줘도 되어야 함

    //when
    boolean result = passwordResetService.selfCertification(businessNumber, name, phoneNumber);

    //then
    assertThat(result).isEqualTo(true);

  }

  @Test
  public void 본인인증_실패_bizNo() {
    //given
    String businessNumber = "1234567890";
    String name = "Lee";
    String phoneNumber = "016-304-6862";

    //when
    Throwable throwable = catchThrowable(() -> passwordResetService.selfCertification(businessNumber, name, phoneNumber));

    //then
    assertThat(throwable)
        .isInstanceOf(InvalidParameterException.class);
  }


  @Test
  public void 로그인전_재설정_성공() throws JsonProcessingException {
    //given
    String businessNumber = "123-33-12345";
    String json = "{\n" +
        " \"newPassword\": \"qq123456!!\",\n" +
        " \"newPasswordCheck\": \"qq123456!!\"\n" +
        "}";
    ObjectMapper objectMapper = new ObjectMapper();
    //when
    PasswordChangeRequest passwordChangeRequest = objectMapper.readValue(json, PasswordChangeRequest.class);
    boolean reset = passwordResetService.reset(businessNumber, passwordChangeRequest);
    assertThat(reset).isEqualTo(true);
  }


}
