package com.tpay.domains.franchisee.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantSaveService;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.refund.application.RefundService;
import com.tpay.domains.vat.application.VatService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class VatServiceTest {

  FranchiseeEntity franchiseeEntity;
  CustomerEntity customerEntity;
  OrderEntity orderEntity;
  @Autowired
  VatService vatService;
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
  RefundService refundService;
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
}
