package com.tpay.domains.sale.application;


import com.tpay.commons.util.DateFilterV2;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SaleAnalysisFindServiceV2Test {

  @Autowired
  private RefundRepository refundRepository;

  private FranchiseeEntity franchiseeEntity;
  private RefundEntity refundEntity;
  private OrderEntity orderEntity;
  private CustomerEntity customerEntity;

  @BeforeEach
  public void setup() {
    franchiseeEntity =
        FranchiseeEntity.builder()
            .sellerName("테스트코드")
            .storeTel("555-5555-5555")
            .businessNumber("121-12'-12121")
            .email("namSK@TestCode.co.kr")
            .password("test1234!@")
            .storeName("매장명")
            .signboard("간판명")
            .storeAddressNumber("12345")
            .storeAddressBasic("경기도 수원시 권선구")
            .storeAddressDetail("권선로")
            .productCategory("아무거나 넣어도 됨")
            .isTaxRefundShop("N")
            .build();
  }

  @Test
  public void 매출분석날짜테스트() {
    //given
    Long franchiseeIndex = 1L;
    DateFilterV2 dateFilterV2 = DateFilterV2.TODAY;
    String startDate = dateFilterV2.getStartDate().replaceAll("-", "");
    String endDate = dateFilterV2.getEndDate().replaceAll("-", "");
//    when
//    List<SaleAnalysisFindResponse> saleAnalysisV2 = refundRepository.findSaleAnalysisV2(franchiseeIndex, startDate, endDate);

    // TODO: 2022/02/08 매출분석 날짜테스트 이어서 계속 할 것

  }


}
