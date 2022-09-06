package com.tpay.domains.category.application;

import com.tpay.domains.category.application.dto.CategoryInfo;
import com.tpay.domains.category.domain.CategoryEntity;
import com.tpay.domains.category.domain.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryFindService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public List<CategoryInfo> findAll() {
        return categoryRepository.findAll().stream().map(CategoryInfo::new).collect(Collectors.toList());
    }
}
