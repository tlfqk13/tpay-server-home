package com.tpay.domains.franchisee.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class FranchiseeRepositoryTest {

  @Autowired private FranchiseeRepository franchiseeRepository;

  @Test
  public void 프렌차이즈_생성_조회_테스트() {
    // given
    FranchiseeEntity franchiseeEntity =
        FranchiseeEntity.builder()
            .memberName("Success")
            .memberNumber("0123-4567")
            .businessNumber("012-34-567")
            .storeName("SuccessMode")
            .storeAddress("Seoul")
            .storeTel("010-1234-1234")
            .productCategory("잡화")
            .build();

    // when
    franchiseeEntity = franchiseeRepository.save(franchiseeEntity);

    // then
    assertThat(franchiseeEntity, is(equalTo(franchiseeRepository.findById(franchiseeEntity.getId()).get())));
  }
}
