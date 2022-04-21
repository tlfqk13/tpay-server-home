package com.tpay.domains.sale.presentation;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.application.RefundSaveService;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.sale.application.SaleAnalysisFindService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SaleAnalysisFindControllerTest {

    @Autowired
    SaleAnalysisFindService saleAnalysisFindServiceV2;
    @Autowired
    RefundSaveService refundSaveService;
    @Autowired
    OrderSaveService orderSaveService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    FranchiseeRepository franchiseeRepository;
    @Autowired
    CustomerRepository customerRepository;

    FranchiseeEntity franchiseeEntity;
    CustomerEntity customerEntity;
    OrderEntity orderEntity;
    RefundEntity refundEntity;


    @BeforeEach
    public void setup() throws JsonProcessingException {
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

        String json = "{\n" +
            "    \"franchiseeIndex\": \"1\",\n" +
            "    \"customerIndex\": \"1\",\n" +
            "    \"price\": \"10000\",\n" +
            "    \"userSelector\": \"FRANCHISEE\"\n" +
            "}";
        RefundSaveRequest refundSaveRequest = objectMapper.readValue(json, RefundSaveRequest.class);
        orderEntity = orderSaveService.save(refundSaveRequest);
        refundEntity = refundSaveService.save("0000", "123412341234", "99999999999", orderEntity);

    }

    //가끔씩 날짜 관련 에러나옴
//  @Test
//  void 매출분석_파라미터맵핑_테스트() {
//    //given
//    Long franchiseeIndex = 1L;
//    DateFilterV2 dateFilterV2 = DateFilterV2.TODAY;
//    String startDate = "";
//    String endDate = "";
//
//    //when
//    List<String> strings = saleAnalysisFindServiceV2.testDateNativeQuery(franchiseeIndex, dateFilterV2, startDate, endDate);
//
//    //then
//    Assertions.assertThat(strings.get(0)).isEqualTo(LocalDate.now().toString());
//
//  }
}
