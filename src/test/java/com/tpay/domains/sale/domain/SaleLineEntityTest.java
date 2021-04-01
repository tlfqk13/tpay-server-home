package com.tpay.domains.sale.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

import com.tpay.domains.product.domain.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaleLineEntityTest {

  private ProductEntity productEntity;

  @BeforeEach
  public void setup() {
    productEntity = ProductEntity.builder().price("20000").build();
  }

  @Test
  public void 판매건_총판매금액_계산_테스트() {
    // given
    SaleLineEntity saleLineEntity =
        SaleLineEntity.builder().productEntity(productEntity).quantity("4").build();

    // when
    String calculatedTotalPrice = saleLineEntity.getTotalPrice();

    // then
    assertThat(calculatedTotalPrice, is("80000"));
  }

  @Test
  public void 판매건_판매금액_부가가치세_계산_테스트() {
    // given
    SaleLineEntity saleLineEntity =
        SaleLineEntity.builder().productEntity(productEntity).quantity("4").build();

    // when
    double calculatedVAT = Double.parseDouble(saleLineEntity.getVat());

    // then
    assertThat(calculatedVAT, is(closeTo(7272.7272, 0.0001)));
  }
}
