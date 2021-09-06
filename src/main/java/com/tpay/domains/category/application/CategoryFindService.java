package com.tpay.domains.category.application;

import com.tpay.domains.category.application.dto.CategoryInfo;
import com.tpay.domains.category.domain.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryFindService {

  private final CategoryRepository categoryRepository;

  @Transactional
  public List<CategoryInfo> findAll() {
    List<CategoryInfo> categoryInfoList =
        categoryRepository.findAll().stream()
            .map(
                categoryEntity ->
                    CategoryInfo.builder()
                        .id(categoryEntity.getId())
                        .name(categoryEntity.getName())
                        .build())
            .collect(Collectors.toList());

    return categoryInfoList;
  }
}
