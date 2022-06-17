package com.tpay.domains.category.application.dto;

import com.tpay.domains.category.domain.CategoryEntity;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryInfo {
    private Long id;
    private String name;

    public CategoryInfo(CategoryEntity categoryEntity){
        this.id = categoryEntity.getId();
        this.name = categoryEntity.getName();
    }
}
