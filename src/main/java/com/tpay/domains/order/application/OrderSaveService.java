package com.tpay.domains.order.application;

import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.product.application.ProductFindService;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSaveService {

  private final OrderRepository orderRepository;
  private final FranchiseeFindService franchiseeFindService;
  private final CustomerFindService customerFindService;
  private final ProductFindService productFindService;
  private final OrderLineSaveService orderLineSaveService;

  @Transactional
  public OrderEntity save(RefundSaveRequest request) {
    FranchiseeEntity franchiseeEntity =
        franchiseeFindService.findByIndex(request.getFranchiseeIndex());

    CustomerEntity customerEntity = customerFindService.findByIndex(request.getCustomerIndex());

    ProductEntity productEntity =
        productFindService.findOrElseSave(
            franchiseeEntity.getProductCategory(), request.getPrice());

    return this.save(franchiseeEntity, customerEntity, productEntity);
  }

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

    orderRepository.save(orderEntity);
    orderLineSaveService.save(orderEntity, productEntity);

    return orderEntity;
  }
}
