package com.tpay.domains.category.application;

import com.tpay.domains.category.application.dto.CategoryResponse;
import com.tpay.domains.category.domain.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryFindService {

  private final CategoryRepository categoryRepository;

  @Transactional
  public ResponseEntity<List<CategoryResponse>> findCategories() {
    List<CategoryResponse> categoryResponseList =
        categoryRepository.findAll().stream()
            .map(
                categoryEntity ->
                    CategoryResponse.builder()
                        .id(categoryEntity.getId())
                        .name(categoryEntity.getName())
                        .build())
            .collect(Collectors.toList());

    return ResponseEntity.ok(categoryResponseList);
  }
}
