package com.tpay.domains.franchisee.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class FranchiseeRepositoryTest {

  @Autowired private FranchiseeRepository franchiseeRepository;
  private FranchiseeEntity franchiseeEntity;

  @BeforeEach
  public void setup() {
    franchiseeEntity =
        FranchiseeEntity.builder()
            .sellerName("테스트코드")
            .storeTel("555-5555-5555")
            .businessNumber("121-12-12121")
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
    franchiseeRepository.save(franchiseeEntity);
  }

  @Test
  public void 프렌차이즈_생성_조회_테스트() {
    // then
    assertThat(
        franchiseeEntity, is(equalTo(franchiseeRepository.findAll().stream().findFirst().get())));
  }

  @Test
  public void 프렌차이즈_사업자등록번호로_가입여부_테스트() {
    // when
    boolean isExists = franchiseeRepository.existsByBusinessNumber("1211212121");
    // then
    assertThat(isExists, is(true));
  }
}
