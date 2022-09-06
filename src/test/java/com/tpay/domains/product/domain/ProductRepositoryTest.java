package com.tpay.domains.product.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class ProductRepositoryTest {

  @Autowired private ProductRepository productRepository;
  private ProductEntity productEntity;

  @BeforeEach
  public void setup() {
    productEntity =
        ProductEntity.builder()
            .name("TestProduct")
            .lineNumber("001")
            .code("A0001")
            .price("20000")
            .build();

    productRepository.save(productEntity);
  }

  @Test
  public void 포인트_단일_조회_테스트() {
    // given

    // when
    ProductEntity savedProductEntity = productRepository.findAll().stream().findFirst().get();

    // then
    assertThat(productEntity, is(savedProductEntity));
  }
}
