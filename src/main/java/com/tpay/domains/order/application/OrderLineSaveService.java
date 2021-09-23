package com.tpay.domains.order.application;

import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderLineEntity;
import com.tpay.domains.order.domain.OrderLineRepository;
import com.tpay.domains.product.domain.ProductEntity;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
