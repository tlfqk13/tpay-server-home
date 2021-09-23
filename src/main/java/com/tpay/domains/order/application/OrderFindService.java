package com.tpay.domains.order.application;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.order.application.dto.OrderFindResponse;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.OrderRepository;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderFindService {
  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final RefundRepository refundRepository;

  @Transactional
  public List<OrderEntity> findAllByFranchiseeEntityIndex(Long franchiseeIndex) {
    return orderRepository
        .findAllByFranchiseeEntityId(franchiseeIndex)
        .orElse(Collections.emptyList());
  }

  @Transactional
  public List<OrderFindResponse> findAllSale(RefundInquiryRequest refundInquiryRequest) {
    CustomerEntity customerEntity =
        customerRepository
            .findByCustomerNameAndPassportNumber(
                refundInquiryRequest.getName(), refundInquiryRequest.getPassportNumber())
            .orElseThrow(() -> new IllegalArgumentException("Invalid Customer"));

    List<OrderEntity> orderEntityList =
        orderRepository.findAllByCustomerEntityId(customerEntity.getId());

    List<OrderFindResponse> orderFindResponseList = new LinkedList<>();
    for (OrderEntity orderEntity : orderEntityList) {
      RefundEntity refundEntity =
          refundRepository.findBySaleEntityIdAndRefundStatus(
              orderEntity.getId(), RefundStatus.APPROVAL);

      if (refundEntity != null) {
        orderFindResponseList.add(
            OrderFindResponse.builder()
                .refundId(refundEntity.getId())
                .saleId(orderEntity.getId())
                .orderNumber(orderEntity.getOrderNumber())
                .saleDate(orderEntity.getSaleDate())
                .totalRefund(refundEntity.getTotalRefund())
                .build());
      }
    }

    return orderFindResponseList;
  }
}
