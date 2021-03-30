package com.tpay.domains.sale.domain;

import com.tpay.domains.customer.domain.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class SaleRepositoryTest {

  @Autowired SaleRepository saleRepository;

  @Test
  public void 판매_생성_테스트() throws Exception {

    CustomerEntity customerEntity = CustomerEntity.builder().customerName("success").nation("ko").passportNumber("3489384").build();

    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    // given
    SaleEntity saleEntity =
        SaleEntity.builder()
            .saleDate(now)
            .orderNumber("1932487633")
            .totalAmount("129000")
            .totalQuantity("1")
            .totalVat("14190")
            .customerEntity(customerEntity)
            .build();

    // when
    saleRepository.save(saleEntity);

    // then
    assertThat(saleRepository.getOne(saleEntity.getId()), is(saleEntity));
  }
}
