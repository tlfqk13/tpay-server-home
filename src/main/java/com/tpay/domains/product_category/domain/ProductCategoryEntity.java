package com.tpay.domains.product_category.domain;

import com.tpay.domains.category.domain.CategoryEntity;
import com.tpay.domains.product.domain.ProductEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_category")
@Entity
@ToString
public class ProductCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @Builder
    public ProductCategoryEntity(ProductEntity productEntity, CategoryEntity categoryEntity) {
        this.productEntity = productEntity;
        this.categoryEntity = categoryEntity;
    }
}
