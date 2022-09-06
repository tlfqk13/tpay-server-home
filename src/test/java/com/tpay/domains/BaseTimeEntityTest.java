package com.tpay.domains;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class BaseTimeEntityTest {

  @Autowired private FranchiseeRepository franchiseeRepository;
  private FranchiseeEntity franchiseeEntity;

  @BeforeEach
  public void setup() {
    franchiseeEntity =
        FranchiseeEntity.builder()
            .businessNumber("012-34-567")
            .storeName("SuccessMode")
            .storeAddressNumber("00011")
            .storeAddressBasic("안양시")
            .storeAddressDetail("평촌동")
            .sellerName("Kim")
            .storeTel("010-1234-1234")
            .productCategory("잡화")
            .password("TestPassword")
            .build();
    franchiseeRepository.save(franchiseeEntity);
  }

//  @Test
//  public void JPA_AUDITING_TEST() {
//    System.out.println("시작");
//    // given
//    LocalDateTime now = LocalDateTime.now();
//
//    // when
//    FranchiseeEntity savedFranchiseeEntity =
//        franchiseeRepository.findAll().stream().findFirst().get();
//
//    // then
//    assertTrue(savedFranchiseeEntity.getCreatedDate().isBefore(now));
//    assertTrue(savedFranchiseeEntity.getModifiedDate().isBefore(now));
//  }
}
