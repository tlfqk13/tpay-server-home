package com.tpay.domains.product.application;

import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ProductFindService {

    private final ProductRepository productRepository;
    private final ProductSaveService productSaveService;

    @Transactional
    public ProductEntity findOrElseSave(String productName, String price, String refund) {
        ProductEntity productEntity =
                productRepository
                        .findByNameAndPrice(productName, price)
                        .orElseGet(() -> productSaveService.save(productName, price, refund));

        // refund column 이 추가되면서 productName 과 price 가 같은 product 중 refund 가 없는 엔티티는 업데이트
        if (null == productEntity.getRefund()) {
            productEntity.updateRefundVal(refund);
        }

        return productEntity;
    }

    @Transactional
    public ProductEntity findOrElseSave(String productName, String price) {
        ProductEntity productEntity =
                productRepository
                        .findByNameAndPrice(productName, price)
                        .orElseGet(() -> productSaveService.save(productName, price));

        return productEntity;
    }
}
