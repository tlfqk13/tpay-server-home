package com.tpay.domains.order.application;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.product.domain.ProductEntity;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSaveService {

  private final OrderRepository orderRepository;
  private final OrderLineSaveService orderLineSaveService;

  @Transactional
  public OrderEntity save(
      FranchiseeEntity franchiseeEntity,
      CustomerEntity customerEntity,
      ProductEntity productEntity) {

    OrderEntity orderEntity =
        OrderEntity.builder()
            .franchiseeEntity(franchiseeEntity)
            .customerEntity(customerEntity)
            .build();

    orderLineSaveService.save(orderEntity, productEntity);

    return orderEntity;
  }
}
