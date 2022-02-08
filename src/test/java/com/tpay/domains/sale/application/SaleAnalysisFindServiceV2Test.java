package com.tpay.domains.sale.application;


import com.tpay.commons.util.DateFilterV2;
import com.tpay.domains.refund.domain.RefundRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SaleAnalysisFindServiceV2Test {

  @Autowired
  private RefundRepository refundRepository;


  @Test
  public void 매출분석날짜테스트_오늘() {
    //given
    Long franchiseeIndex = 4L;
    DateFilterV2 dateFilterV2 = DateFilterV2.TODAY;
    String startDate = dateFilterV2.getStartDate().replaceAll("-", "");
    String endDate = dateFilterV2.getEndDate().replaceAll("-", "");

    System.out.println(startDate);
    System.out.println(endDate);
    // TODO: 2022/02/08 실제 쿼리 테스트 해볼것
    //when
//    List<SaleAnalysisFindResponse> saleAnalysisV2 = refundRepository.findSaleAnalysisV2(franchiseeIndex, startDate, endDate);

  }


}
