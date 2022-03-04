package com.tpay.domains.franchisee.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.application.dto.vat.FranchiseeVatReportResponseInterface;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantSaveService;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.refund.application.RefundSaveService;
import com.tpay.domains.refund.domain.RefundEntity;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
public class FranchiseeVatServiceTest {

  FranchiseeEntity franchiseeEntity;
  CustomerEntity customerEntity;
  OrderEntity orderEntity;
  @Autowired
  FranchiseeVatService franchiseeVatService;
  @Autowired
  OrderRepository orderRepository;
  @Autowired
  private FranchiseeRepository franchiseeRepository;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  OrderSaveService orderSaveService;
  @Autowired
  RefundSaveService refundSaveService;
  @Autowired
  FranchiseeApplicantSaveService franchiseeApplicantSaveService;
  @Autowired
  PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
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
    franchiseeRepository.save(franchiseeEntity);
    customerEntity = CustomerEntity.builder()
        .nation("KOR")
        .customerName("NSK")
        .passportNumber("99999999999999")
        .build();
    customerRepository.save(customerEntity);
    orderEntity = OrderEntity.builder()
        .customerEntity(customerEntity)
        .franchiseeEntity(franchiseeEntity)
        .build();
    orderRepository.save(orderEntity);
  }


  @Test
  public void vat조회_Null() {
    //given
    Long franchiseeIndex = 1L;
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = LocalDate.now();

    //when
    FranchiseeVatReportResponseInterface result = orderRepository.findQuarterlyVatReport(franchiseeIndex, startDate, endDate);
    assertThat(result).isEqualTo(null);
  }

  @Test
  public void 날짜셋업_에러() {
    //given
    String requestDate = "220";
    //when
    Throwable throwable = catchThrowable(() -> franchiseeVatService.setUpDate(requestDate));
    //then
    assertThat(throwable).isInstanceOf(InvalidParameterException.class);
  }
}
