package com.tpay.domains.category.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local", "test"})
class CategoryRepositoryTest {

  @Autowired private CategoryRepository categoryRepository;

  @Test
  public void 카테고리_단일_조회_테스트() {
    // given

    // when
    CategoryEntity savedCategoryEntity = categoryRepository.findAll().stream().findFirst().get();

    // then
    assertThat(savedCategoryEntity.getName(), is(equalTo("잡화")));
  }

  @Test
  public void 카테고리_리스트_조회_테스트() {
    // given

    // when
    List<CategoryEntity> categoryEntityList = categoryRepository.findAll();

    // then
    assertThat(categoryEntityList.size(), is(6));
    assertThat(categoryEntityList.get(0).getName(), is("잡화"));
  }
}
