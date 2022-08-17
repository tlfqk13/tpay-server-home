package com.tpay.domains.order.application;

import com.tpay.domains.customer.application.CustomerUpdateService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderLineEntity;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.product.application.ProductFindService;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class OrderSaveService {

    private final OrderRepository orderRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final CustomerUpdateService customerUpdateService;
    private final ProductFindService productFindService;
    private final OrderLineSaveService orderLineSaveService;

    @Transactional
    public OrderEntity save(RefundSaveRequest request) {
        FranchiseeEntity franchiseeEntity =
            franchiseeFindService.findByIndex(request.getFranchiseeIndex());

        CustomerEntity customerEntity = customerUpdateService.findByIndex(request.getCustomerIndex());

        ProductEntity productEntity =
            productFindService.findOrElseSave(
                franchiseeEntity.getProductCategory(), request.getPrice());

        return this.save(franchiseeEntity, customerEntity, productEntity);
    }

    @Transactional
    public OrderEntity save(ExternalRefundEntity externalRefundEntity, String amount) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(externalRefundEntity.getFranchiseeIndex());
        CustomerEntity customerEntity = customerUpdateService.findByIndex(externalRefundEntity.getCustomerIndex());
        ProductEntity productEntity = productFindService.findOrElseSave(franchiseeEntity.getProductCategory(), amount);
        return this.save(franchiseeEntity, customerEntity, productEntity);

    }

    @Transactional
    public OrderEntity save(
        FranchiseeEntity franchiseeEntity,
        CustomerEntity customerEntity,
        ProductEntity productEntity
    ) {

        OrderEntity orderEntity =
            OrderEntity.builder()
                .franchiseeEntity(franchiseeEntity)
                .customerEntity(customerEntity)
                .build();

        orderRepository.save(orderEntity);
        OrderLineEntity orderLineEntity = orderLineSaveService.save(orderEntity, productEntity);
        orderEntity.addOrderLine(orderLineEntity);

        return orderEntity;
    }
}
