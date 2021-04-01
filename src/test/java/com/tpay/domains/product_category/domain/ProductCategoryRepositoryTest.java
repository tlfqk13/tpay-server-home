package com.tpay.domains.product_category.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.tpay.domains.category.domain.CategoryEntity;
import com.tpay.domains.category.domain.CategoryRepository;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class ProductCategoryRepositoryTest {

  @Autowired private ProductCategoryRepository productCategoryRepository;
  @Autowired private ProductRepository productRepository;
  @Autowired private CategoryRepository categoryRepository;

  private ProductCategoryEntity productCategoryEntity;
  private ProductEntity productEntity;
  private CategoryEntity categoryEntity;

  @BeforeEach
  public void setup() {
    productEntity = ProductEntity.builder().build();
    categoryEntity = CategoryEntity.builder().build();

    productRepository.save(productEntity);
    categoryRepository.save(categoryEntity);

    productCategoryEntity =
        ProductCategoryEntity.builder()
            .productEntity(productEntity)
            .categoryEntity(categoryEntity)
            .build();

    productCategoryRepository.save(productCategoryEntity);
  }

  @Test
  public void 품목_카테고리_관계데이터_생성_조회_테스트() {
    // given

    // when
    ProductCategoryEntity savedProductCategoryEntity =
        productCategoryRepository.findAll().stream().findFirst().get();

    // then
    // TODO : product_category 테이블과 각 관계 테이블을 조인하여 조회를 추측했으나 그렇지 않음
    assertThat(productCategoryEntity, is(savedProductCategoryEntity));
    assertThat(productEntity, is(savedProductCategoryEntity.getProductEntity()));
    assertThat(categoryEntity, is(savedProductCategoryEntity.getCategoryEntity()));
  }
}
