package com.tpay.domains.refund.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(properties = "spring.config.location=src/test/resources/application.yml")
public class RefundAPITest {

  @Autowired
  FranchiseeRepository franchiseeRepository;
  @Autowired
  ObjectMapper mapper;


  RefundLimitRequest request = RefundLimitRequest.builder()
      .name("SUCCESSMODEK")
      .passportNumber("SUCCESS20")
      .nationality("GBR")
      .totalAmount("0")
      .saleDate("20211028133004192")
      .build();
  @Value("${custom.refund.server}")
  private String REFUND_SERVER;

  @Test
  public void 환급_조회_외부_API_TEST() throws JsonProcessingException {
    //when, then
    WebTestClient.bindToServer()
        .baseUrl(REFUND_SERVER)
        .build()
        .post()
        .uri("/refund/limit")
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .jsonPath("responseCode")
        .isEqualTo("0000");

  }
}
