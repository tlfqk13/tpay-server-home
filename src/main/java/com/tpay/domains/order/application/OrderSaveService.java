package com.tpay.domains.order.application;

import com.tpay.commons.util.IndexInfo;
import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.dto.OrderDto;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderLineEntity;
import com.tpay.domains.order.domain.OrderRepository;
import com.tpay.domains.product.application.ProductFindService;
import com.tpay.domains.product.domain.ProductEntity;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;

@Service
@RequiredArgsConstructor
public class OrderSaveService {

    private final OrderRepository orderRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final EmployeeFindService employeeFindService;
    private final CustomerService customerService;
    private final ProductFindService productFindService;
    private final OrderLineSaveService orderLineSaveService;

    @Transactional
    public OrderEntity save(RefundSaveRequest request) {
        FranchiseeEntity franchiseeEntity =
            franchiseeFindService.findByIndex(request.getFranchiseeIndex());

        CustomerEntity customerEntity = customerService.findByIndex(request.getCustomerIndex());

        ProductEntity productEntity =
            productFindService.findOrElseSave(
                franchiseeEntity.getProductCategory(), request.getPrice(),request.getRefund());

        return this.save(franchiseeEntity, customerEntity, productEntity, null);
    }

    @Transactional
    public OrderEntity save(ExternalRefundEntity externalRefundEntity, String amount) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(externalRefundEntity.getFranchiseeIndex());
        CustomerEntity customerEntity = customerService.findByIndex(externalRefundEntity.getCustomerIndex());
        ProductEntity productEntity = productFindService.findOrElseSave(franchiseeEntity.getProductCategory(), amount);
        return this.save(franchiseeEntity, customerEntity, productEntity, null);

    }

    @Transactional
    public OrderEntity save(
        FranchiseeEntity franchiseeEntity,
        CustomerEntity customerEntity,
        ProductEntity productEntity,
        String purchaseSn
    ) {

        OrderEntity orderEntity =
            OrderEntity.builder()
                .franchiseeEntity(franchiseeEntity)
                .customerEntity(customerEntity)
                .purchaseSn(purchaseSn)
                .build();

        orderRepository.save(orderEntity);
        OrderLineEntity orderLineEntity = orderLineSaveService.save(orderEntity, productEntity);
        orderEntity.addOrderLine(orderLineEntity);

        return orderEntity;
    }

    @Transactional
    public OrderDto.Response createOrder(OrderDto.Request orderDto, IndexInfo indexInfo) {
        Long franchiseIndex;
        if(EMPLOYEE == indexInfo.getUserSelector()) {
            EmployeeEntity employee = employeeFindService.findById(Long.parseLong(indexInfo.getIndex()))
                    .orElseThrow();
            franchiseIndex = employee.getFranchiseeEntity().getId();
        } else {
            franchiseIndex = Long.parseLong(indexInfo.getIndex());
        }

        FranchiseeEntity franchisee = franchiseeFindService.findByIndex(franchiseIndex);
        CustomerEntity customer = customerService.findByIndex(orderDto.getCustomerIdx());
        ProductEntity productEntity =
                productFindService.findOrElseSave(
                        franchisee.getProductCategory(), orderDto.getPrice(),orderDto.getRefund());

        OrderEntity savedOrder = save(franchisee, customer, productEntity, createPurchaseSn());

        return new OrderDto.Response(savedOrder.getOrderNumber());
    }

    private String createPurchaseSn() {
        return "990" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
