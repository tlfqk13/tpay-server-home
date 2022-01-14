package com.tpay.domains.customer.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class CustomerRepositoryTest {
  @Autowired CustomerRepository customerRepository;

  @Test
  public void 구매자_생성_테스트() {
    // given
    CustomerEntity customerEntity =
        customerRepository.save(
            CustomerEntity.builder()
                .passportNumber("A123456789")
                .nation("KOR")
                .customerName("AEJEONG SHIN")
                .build());

    // when
    List<CustomerEntity> customerEntityList = customerRepository.findAll();

    // then
    assertThat(customerEntityList.stream().findFirst().get(), is(equalTo(customerEntity)));
  }

  @Test
  public void 구매자_생성_null_테스트() {
    // given
    CustomerEntity customerEntity =
        CustomerEntity.builder()
            .nation("KOR")
            .passportNumber(null)
            .customerName("AEJEONG SHIN")
            .build();

    // 제약조건 위반 Exception
    assertThrows(
        ConstraintViolationException.class, () -> customerRepository.save(customerEntity));
  }
}
