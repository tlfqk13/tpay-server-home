package com.tpay.domains.sale.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class SaleRepositoryTest {

  @Autowired SaleRepository saleRepository;
  @Autowired FranchiseeRepository franchiseeRepository;
  @Autowired CustomerRepository customerRepository;

  CustomerEntity customerEntity;
  FranchiseeEntity franchiseeEntity;
  String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
  String businessNumber = ("123-45-67890").replace("-", "");

  @BeforeEach
  public void setup() {
    franchiseeEntity =
        franchiseeRepository.save(
            FranchiseeEntity.builder().businessNumber(businessNumber).build());
    customerEntity =
        customerRepository.save(
            CustomerEntity.builder()
                .customerName("success")
                .nation("ko")
                .passportNumber("3489384")
                .build());
  }

  @Test
  public void 주문_번호_생성_테스트() {

    // given
    Random random = new Random();
    int iValue = (random.nextInt(99));
    String orderNumber = iValue + now;

    // when
    SaleEntity saleEntity =
        SaleEntity.builder()
            .saleDate(now)
            .orderNumber(orderNumber)
            .totalAmount("129000")
            .totalQuantity("1")
            .totalVat("14190")
            .customerEntity(customerEntity)
            .franchiseeEntity(franchiseeEntity)
            .build();

    // then
    System.out.println("TEST orderNumber : " + orderNumber);
    assertThat(saleEntity.getOrderNumber(), is(equalTo(orderNumber)));
  }

  @Test
  public void 판매_생성_테스트() {

    // given
    SaleEntity saleEntity =
        saleRepository.save(
            SaleEntity.builder()
                .saleDate(now)
                .orderNumber("1932487633")
                .totalAmount("129000")
                .totalQuantity("1")
                .totalVat("14190")
                .customerEntity(customerEntity)
                .franchiseeEntity(franchiseeEntity)
                .build());

    // when
    List<SaleEntity> saleEntityList = saleRepository.findAll();

    // then
    assertThat(saleEntityList.stream().findFirst().get(), is(equalTo(saleEntity)));
  }
}
