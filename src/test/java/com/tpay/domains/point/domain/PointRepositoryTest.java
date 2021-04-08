package com.tpay.domains.point.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class PointRepositoryTest {

  FranchiseeEntity franchiseeEntity;
  @Autowired private PointRepository pointRepository;
  @Autowired private FranchiseeRepository franchiseeRepository;

  @BeforeEach
  public void setup() {
    franchiseeEntity =
        FranchiseeEntity.builder()
            .businessNumber("012-34-567")
            .storeName("SuccessMode")
            .storeAddress("Seoul")
            .sellerName("Kim")
            .storeTel("010-1234-1234")
            .productCategory("잡화")
            .password("TestPassowrd")
            .build();

    franchiseeRepository.save(franchiseeEntity);
  }

  @Test
  public void 프렌차이즈_주어진경우_포인트_등록_적립() {
    // given
    SignType signType = SignType.POSITIVE;
    long change = 20000L;

    FranchiseeEntity savedFranchiseeEntity =
        franchiseeRepository.findAll().stream().findFirst().get();
    savedFranchiseeEntity.changeBalance(signType, change);

    PointEntity pointEntity =
        PointEntity.builder()
            .signType(SignType.POSITIVE)
            .change(20000L)
            .pointStatus(PointStatus.SAVE)
            .balance(savedFranchiseeEntity.getBalance())
            .franchiseeEntity(savedFranchiseeEntity)
            .build();

    // when
    pointRepository.save(pointEntity);

    // then
    PointEntity savedPointEntity = pointRepository.findAll().stream().findFirst().get();

    assertThat(pointEntity, is(equalTo(savedPointEntity)));
    assertThat(savedPointEntity.getBalance(), is(equalTo(20000L)));
  }
}
