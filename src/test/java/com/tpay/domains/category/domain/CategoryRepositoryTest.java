package com.tpay.domains.category.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class CategoryRepositoryTest {

  @Autowired private CategoryRepository categoryRepository;
  private CategoryEntity categoryEntity;

  @BeforeEach
  public void setup() {
    categoryEntity = CategoryEntity.builder().name("잡화").build();
    categoryRepository.save(categoryEntity);
  }

  @Test
  public void 카테고리_단일_조회_테스트() {
    // given

    // when
    CategoryEntity savedCategoryEntity =
        categoryRepository.findAll().stream().findFirst().get();

    // then
    assertThat(categoryEntity, is(equalTo(savedCategoryEntity)));
  }
}
