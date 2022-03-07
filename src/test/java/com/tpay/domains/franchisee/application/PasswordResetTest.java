package com.tpay.domains.franchisee.application;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.dto.FranchiseeSignUpRequest;
import com.tpay.domains.franchisee.application.dto.PasswordChangeRequest;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
public class PasswordResetTest {
  @Autowired
  FranchiseeSignUpService franchiseeSignUpService;
  @Autowired
  PasswordResetService passwordResetService;
  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  FranchiseeRepository franchiseeRepository;

  FranchiseeEntity franchiseeEntity;
  Long franchiseeId;
  String businessNumber;
  FranchiseeEntity savedFranchiseeEntity;

  @Autowired
  ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    String json = "{\n" +
        "    \"sellerName\": \"동시성테스트\",\n" +
        "    \"storeTel\": \"010-0101-1010\",\n" +
        "    \"businessNumber\": \"333-33-33333\",\n" +
        "    \"email\": \"dondsi@sungtae.st\",\n" +
        "    \"password\": \"qq123456!!\",\n" +
        "\n" +
        "    \"storeName\": \"이십오일테스트\",\n" +
        "    \"signboard\": \"이십오일간판명\",\n" +
        "    \"storeAddressNumber\": \"01133\",\n" +
        "    \"storeAddressBasic\": \"기본 안양 주소\",\n" +
        "    \"storeAddressDetail\": \"상세 안양 302 주소\",\n" +
        "    \"productCategory\": \"아이맥\",\n" +
        "    \"storeNumber\": \"123-5678-6666\",\n" +
        "    \"isTaxRefundShop\":\"Y\"\n" +
        "}";
    try {
      FranchiseeSignUpRequest franchiseeSignUpRequest = objectMapper.readValue(json, FranchiseeSignUpRequest.class);
      franchiseeSignUpService.signUp(franchiseeSignUpRequest);
    } catch (Exception e) {

    }
    savedFranchiseeEntity = franchiseeRepository.findById(1L).get();
    franchiseeId = savedFranchiseeEntity.getId();
    businessNumber = savedFranchiseeEntity.getBusinessNumber();
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
    String businessNumber = "3-33333333-3"; //이상하게 줘도 되어야 함
    String name = "동시성테스트";
    String phoneNumber = "010-010110-10"; //이상해게 줘도 되어야 함

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
    String businessNumber = "333-33-33333";
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

  @Test
  public void 재설정후_조회성공() throws JsonProcessingException{
    //given
    String businessNumber = "333-33-33333";
    String json = "{\n" +
        " \"newPassword\": \"abcde123!!\",\n" +
        " \"newPasswordCheck\": \"abcde123!!\"\n" +
        "}";
    ObjectMapper objectMapper = new ObjectMapper();
    //when
    PasswordChangeRequest passwordChangeRequest = objectMapper.readValue(json, PasswordChangeRequest.class);
    boolean reset = passwordResetService.reset(businessNumber, passwordChangeRequest);
    System.out.println(reset);
    Optional<FranchiseeEntity> byBusinessNumber = franchiseeRepository.findByBusinessNumber(businessNumber.replaceAll("-", ""));
    String updatedPassword = byBusinessNumber.get().getPassword();

    boolean matches = passwordEncoder.matches("abcde123!!", updatedPassword);
    assertThat(matches).isEqualTo(true);

  }


}
