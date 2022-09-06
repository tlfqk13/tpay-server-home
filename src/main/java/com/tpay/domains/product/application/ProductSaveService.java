package com.tpay.domains.product.application;

import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSaveService {

    private final ProductRepository productRepository;

    public ProductEntity save(String category, String price) {
        ProductEntity productEntity =
            ProductEntity.builder().name(category).code("001").lineNumber("001").price(price).build();
        return productRepository.save(productEntity);
    }
}
