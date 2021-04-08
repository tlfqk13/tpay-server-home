package com.tpay.domains.sale.domain;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class SaleRepositoryTest {

  @Autowired SaleRepository saleRepository;
  @Autowired FranchiseeRepository franchiseeRepository;
  @Autowired CustomerRepository customerRepository;
  @Autowired SaleLineRepository saleLineRepository;
  @Autowired ProductRepository productRepository;

  CustomerEntity customerEntity;
  FranchiseeEntity franchiseeEntity;

  String businessNumber = ("123-45-67890").replace("-", "");

  @BeforeEach
  public void setup() {
    franchiseeEntity =
        franchiseeRepository.save(
            FranchiseeEntity.builder()
                .storeTel("00000")
                .storeName("SUCCESSMODE")
                .storeAddress("안양")
                .sellerName("주병천")
                .productCategory("모자")
                .memberNumber("0000")
                .memberName("주병천")
                .password("0000")
                .businessNumber(businessNumber)
                .build());

    customerEntity =
        customerRepository.save(
            CustomerEntity.builder()
                .customerName("success")
                .nation("ko")
                .passportNumber("3489384")
                .build());
  }

  @Test
  public void 판매_생성_테스트() {

    // given

    ProductEntity productEntity =
        productRepository.save(
            ProductEntity.builder()
                .code("E112")
                .lineNumber("1")
                .name("BAG")
                .price("20000")
                .build());

    List<SaleLineEntity> saleLineEntityList = new LinkedList<>();
    saleLineEntityList.add(
        SaleLineEntity.builder().productEntity(productEntity).quantity("4").build());

    // when

    SaleEntity saleEntity =
        saleRepository.save(
            SaleEntity.builder()
                .customerEntity(customerEntity)
                .franchiseeEntity(franchiseeEntity)
                .saleLineEntity(saleLineEntityList)
                .build());

    saleLineRepository.save(
        SaleLineEntity.builder()
            .productEntity(productEntity)
            .quantity("4")
            .saleEntity(saleEntity)
            .build());

    List<SaleEntity> saleEntityList = saleRepository.findAll();

    // then
    assertThat(saleEntityList.stream().findFirst().get(), is(equalTo(saleEntity)));
  }
}
