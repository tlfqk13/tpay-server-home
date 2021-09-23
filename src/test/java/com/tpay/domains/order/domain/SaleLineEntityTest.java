package com.tpay.domains.order.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
    OrderLineEntity orderLineEntity =
        OrderLineEntity.builder().productEntity(productEntity).quantity("4").build();

    // when
    String calculatedTotalPrice = orderLineEntity.getTotalPrice();

    // then
    assertThat(calculatedTotalPrice, is("80000"));
  }

  @Test
  public void 판매건_판매금액_부가가치세_계산_테스트() {
    // given
    OrderLineEntity orderLineEntity =
        OrderLineEntity.builder().productEntity(productEntity).quantity("4").build();

    // when
    Long calculatedVAT = Long.parseLong(orderLineEntity.getVat());

    // then
    assertThat(calculatedVAT, is(7272L));
  }
}
