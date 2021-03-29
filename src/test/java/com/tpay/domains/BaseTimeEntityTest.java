package com.tpay.domains;

import static org.junit.jupiter.api.Assertions.*;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BaseTimeEntityTest {

  @Autowired
  private FranchiseeRepository franchiseeRepository;

  @Test
  public void JPA_AUDITING_TEST() {
    // given
    LocalDateTime now = LocalDateTime.now();
    franchiseeRepository.save(FranchiseeEntity.builder().storeName("SuccessMode").build());

    // when
    FranchiseeEntity savedFranchiseeEntity = franchiseeRepository.findById(1L).get();

    // then
    assertTrue(savedFranchiseeEntity.getCreatedDate().isAfter(now));
    assertTrue(savedFranchiseeEntity.getModifiedDate().isAfter(now));
  }
}