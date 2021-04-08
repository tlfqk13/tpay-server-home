package com.tpay.domains.refund.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.domains.category.domain.CategoryEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.refund.application.dto.RefundApprovalRequest;
import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.refund.application.dto.RefundRequestProductInfoList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest(properties = "spring.config.location=src/test/resources/application.yml")
public class RefundAPITest {

  @Autowired FranchiseeRepository franchiseeRepository;
  @Autowired ObjectMapper mapper;

  String passportNumber = "SUCCESS09";
  String customerName = "SUCCESSMODE";
  String nationality = "CHN";
  String orderNumber = "G302010241900032997";

  @Value("${custom.refund.server}")
  private String REFUND_SERVER;

  @Test
  public void 환급_조회_외부_API_TEST() throws JsonProcessingException {

    // given
    RefundInquiryRequest refundInquiryRequest =
        mapper.readValue(
            "{\"passportNumber\":\"SUCCESS09\",\"name\":\"SUCCESSMODE\",\"nationality\":\"CHN\",\"orderNumber\":\"G302010241900032997\",\"amount\":\"1000\"}",
            RefundInquiryRequest.class);

    WebTestClient.bindToServer()
        .baseUrl(REFUND_SERVER)
        .build()
        .post()
        .uri("/refund/limit/inquiry")
        .bodyValue(refundInquiryRequest)
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody()
        .jsonPath("responseCode")
        .isEqualTo("0000");

  }

  @Test
  public void 환급_신청_외부_API_TEST() {
    // given

    CategoryEntity categoryEntity = CategoryEntity.builder().name("기타").build();

    ProductEntity productEntity =
        ProductEntity.builder()
            .price("10000")
            .lineNumber("242")
            .code("234")
            .name(categoryEntity.getName())
            .build();

    List<RefundRequestProductInfoList> productInfoList = new LinkedList<>();
    productInfoList.add(
        RefundRequestProductInfoList.builder()
            .productName(productEntity.getName())
            .productPrice("10000")
            .productQuantity("1")
            .productSequenceNumber(productEntity.getLineNumber())
            .productCode(productEntity.getCode())
            .salePrice(productEntity.getPrice())
            .indVAT("900")
            .indIct("0")
            .indEdut("0")
            .indStr("0")
            .build());

    FranchiseeEntity franchiseeEntity =
        FranchiseeEntity.builder()
            .businessNumber("2390401226")
            .productCategory("기타")
            .sellerName("주병천")
            .storeAddress("경기 안양시 동안구 시민대로 327번길 11-41, 3층 302")
            .storeName("SuccessMode")
            .storeTel("0260918011")
            .build();

    RefundApprovalRequest refundApprovalRequest =
        RefundApprovalRequest.builder()
            .businessNumber(franchiseeEntity.getBusinessNumber())
            .franchiseeNumber(franchiseeEntity.getMemberNumber())
            .sellerName(franchiseeEntity.getSellerName())
            .storeName(franchiseeEntity.getStoreName())
            .storeAddress(franchiseeEntity.getStoreAddress())
            .storeTel(franchiseeEntity.getStoreTel())
            .productList("1")
            .productListNow("1")
            .name(customerName)
            .orderNumber(orderNumber)
            .nationality(nationality)
            .amount("10000")
            .passportNumber(passportNumber)
            .totalRefund("900")
            .totalVAT("900")
            .totalIct("0")
            .totalStr("0")
            .totalEdut("0")
            .totalQuantity("1")
            .refundRequestProductInfoList(productInfoList)
            .build();

    WebTestClient.bindToServer()
        .baseUrl(REFUND_SERVER)
        .build()
        .post()
        .uri("/refund/approval")
        .bodyValue(refundApprovalRequest)
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .jsonPath("errorCode")
        .isEqualTo("2011");
    // 이미 승인된 건이 맞으면 OK
  }
}
