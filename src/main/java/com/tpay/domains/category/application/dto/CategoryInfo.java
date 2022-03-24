package com.tpay.domains.category.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryInfo {
    private Long id;
    private String name;
}
