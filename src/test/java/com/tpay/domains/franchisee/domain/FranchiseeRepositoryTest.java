//package com.tpay.domains.franchisee.domain;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//@DataJpaTest
//@ActiveProfiles(profiles = {"local", "test"})
//class FranchiseeRepositoryTest {
//
//  @Autowired private FranchiseeRepository franchiseeRepository;
//  private FranchiseeEntity franchiseeEntity;
//
//  @BeforeEach
//  public void setup() {
//    franchiseeEntity =
//        FranchiseeEntity.builder()
//            .businessNumber("012-34-567")
//            .storeName("SuccessMode")
//            .storeAddress("Seoul")
//            .sellerName("Kim")
//            .storeTel("010-1234-1234")
//            .productCategory("잡화")
//            .password("TestPassword")
//            .build();
//    franchiseeRepository.save(franchiseeEntity);
//  }
//
//  @Test
//  public void 프렌차이즈_생성_조회_테스트() {
//    // then
//    assertThat(
//        franchiseeEntity, is(equalTo(franchiseeRepository.findAll().stream().findFirst().get())));
//  }
//
//  @Test
//  public void 프렌차이즈_사업자등록번호로_가입여부_테스트() {
//    // when
//    boolean isExists = franchiseeRepository.existsByBusinessNumber("012-34-567");
//
//    // then
//    assertThat(isExists, is(true));
//  }
//}
