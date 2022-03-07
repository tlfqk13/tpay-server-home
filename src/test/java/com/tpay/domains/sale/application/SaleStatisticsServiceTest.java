package com.tpay.domains.sale.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.domains.auth.application.dto.SignInTokenInfo;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantSaveService;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.application.RefundSaveService;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund.domain.RefundEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class SaleStatisticsServiceTest {

  FranchiseeEntity franchiseeEntity;
  CustomerEntity customerEntity;
  OrderEntity orderEntity;
  RefundEntity refundEntity;
  String accessToken;
  @Autowired
  private MockMvc mockMvc;
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
  public void setup() throws Exception {
    franchiseeEntity =
        FranchiseeEntity.builder()
            .businessNumber("123-33-12345")
            .storeName("SuccessMode")
            .storeAddressNumber("00011")
            .storeAddressBasic("통계테스트")
            .storeAddressDetail("통계테스트")
            .sellerName("Kim")
            .storeTel("01012341234")
            .productCategory("통계테스트")
            .password(passwordEncoder.encode("qq123456!!"))
            .signboard("간판")
            .storeNumber("031-234-2345")
            .email("abc@defg.co.kr")
            .isTaxRefundShop("false")
            .build();
    franchiseeRepository.save(franchiseeEntity);
    franchiseeApplicantSaveService.save(franchiseeEntity);

    customerEntity = CustomerEntity.builder()
        .nation("KOR")
        .customerName("NSK")
        .passportNumber("99999999999999")
        .build();
    customerRepository.save(customerEntity);

    String json = "{\n" +
        "    \"franchiseeIndex\": \"1\",\n" +
        "    \"customerIndex\": \"1\",\n" +
        "    \"price\": \"10000\"\n" +
        "}";
    RefundSaveRequest refundSaveRequest = objectMapper.readValue(json, RefundSaveRequest.class);
    orderEntity = orderSaveService.save(refundSaveRequest);
    refundEntity = refundSaveService.save("0000", "123412341234", "99999999999", orderEntity);

    String signInData = "{\"userSelector\": \"FRANCHISEE\",\"businessNumber\": \"123-33-12345\",\"password\": \"qq123456!!\"}";

    MvcResult mvcResult = mockMvc.perform(post("/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signInData))
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();
    SignInTokenInfo signInTokenInfo = objectMapper.readValue(contentAsString, SignInTokenInfo.class);
    accessToken = signInTokenInfo.getAccessToken();

  }


  @Test
  public void 매출통계_기본조회() throws Exception {
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    String nowYearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYYMM"));
    param.add("targetDate", nowYearMonth);
    param.add("dateSelector", "MONTH");
    System.out.println();
    Optional<FranchiseeEntity> franchiseeEntity = franchiseeRepository.findById(1L);
    Assertions.assertThat(franchiseeEntity.get().getStoreName()).isEqualTo("이십오일테스트");


    mockMvc.perform(get("/sales/statistics/1")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", accessToken)
            .params(param))
        .andExpect(status().isOk());
  }

}
