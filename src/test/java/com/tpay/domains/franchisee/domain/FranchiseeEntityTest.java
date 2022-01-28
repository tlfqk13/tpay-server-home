package com.tpay.domains.franchisee.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tpay.domains.point.domain.SignType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FranchiseeEntityTest {

  FranchiseeEntity franchiseeEntity;

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
    franchiseeEntity.changeBalance(SignType.POSITIVE, 20000L);
  }

  @Test
  public void 프렌차이즈_포인트_적립_테스트() {
    // given

    // when
    franchiseeEntity.changeBalance(SignType.POSITIVE, 10000L);

    // then
    assertThat(franchiseeEntity.getBalance(), is(30000L));
  }

  @Test
  public void 프렌차이즈_포인트_출금_테스트() {
    // given

    // when
    franchiseeEntity.changeBalance(SignType.NEGATIVE, 5000L);

    // then
    assertThat(franchiseeEntity.getBalance(), is(15000L));
  }

  @Test
  public void 프렌차이즈_포인트_출금금액_잔액보다큰_실패_예외_테스트() {
    // given

    // when
    // then
    assertThrows(
        IllegalArgumentException.class,
        () -> franchiseeEntity.changeBalance(SignType.NEGATIVE, 30000L));
  }
}
