package com.tpay.domains.order.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.IndexInfo;
import com.tpay.domains.api.domain.vo.ApprovalDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSaveService {

    private final OrderRepository orderRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final EmployeeFindService employeeFindService;
    private final CustomerService customerService;
    private final ProductFindService productFindService;
    private final OrderLineSaveService orderLineSaveService;

    @Transactional
    public OrderEntity save(RefundSaveRequest request, Long franchiseeIndex) {

        FranchiseeEntity franchiseeEntity =
                franchiseeFindService.findByIndex(franchiseeIndex);

        CustomerEntity customerEntity = customerService.findByIndex(request.getCustomerIndex());

        ProductEntity productEntity;

        // 환급요율표 미업데이트 가맹점 대상
        if(request.getRefund() == null) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Refund Update did not Franchisee");
        }else {
            productEntity =
                    productFindService.findOrElseSave(
                            franchiseeEntity.getProductCategory(), request.getPrice(), request.getRefund());
            log.trace(" @@ request.getPrice() = {}", request.getPrice());
            log.trace(" @@ request.getRefund() = {}", request.getRefund());
        }
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
    public OrderEntity save(Long customerIdx, ApprovalDto.Request dto) {
        FranchiseeEntity franchiseeEntity =
                franchiseeFindService.findByIndex(Long.parseLong(dto.getFranchiseeId()));

        CustomerEntity customerEntity = customerService.findByIndex(customerIdx);

        ProductEntity productEntity =
                productFindService.findOrElseSave(
                        franchiseeEntity.getProductCategory(), dto.getPrice(), dto.getRefund());

        return this.save(franchiseeEntity, customerEntity, productEntity, null);
    }

    @Transactional
    public OrderDto.Response createOrder(OrderDto.Request orderDto, IndexInfo indexInfo) {
        Long franchiseIndex;
        if (EMPLOYEE == indexInfo.getUserSelector()) {
            EmployeeEntity employee = employeeFindService.findById(indexInfo.getIndex())
                    .orElseThrow();
            franchiseIndex = employee.getFranchiseeEntity().getId();
        } else {
            franchiseIndex = indexInfo.getIndex();
        }
        FranchiseeEntity franchisee = franchiseeFindService.findByIndex(franchiseIndex);
        CustomerEntity customer = customerService.findByIndex(orderDto.getCustomerIndex());
        customer.updateDepartureStatus();
        log.trace(" @@  updateRefundAfterCustomer @@ ");
        log.trace(" @@ customer= {}", customer.getNation());
        log.trace(" @@ orderDto = {}", orderDto.getPrice());

        ProductEntity productEntity =
                productFindService.findOrElseSave(
                        franchisee.getProductCategory(), orderDto.getPrice(), orderDto.getRefund());

        OrderEntity savedOrder = save(franchisee, customer, productEntity, createPurchaseSn());

        return new OrderDto.Response(savedOrder.getOrderNumber());
    }
    private String createPurchaseSn() {
        return "990" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
