package com.tpay.domains.order.application;

import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderLineEntity;
import com.tpay.domains.order.domain.OrderLineRepository;
import com.tpay.domains.product.domain.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class OrderLineSaveService {

    private final OrderLineRepository orderLineRepository;

    @Transactional
    public OrderLineEntity save(OrderEntity orderEntity, ProductEntity productEntity) {
        OrderLineEntity orderLineEntity =
            OrderLineEntity.builder()
                .quantity("1")
                .orderEntity(orderEntity)
                .productEntity(productEntity)
                .build();
        return orderLineRepository.save(orderLineEntity);
    }
}
