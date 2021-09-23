package com.tpay.domains.refund.application;

import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.order.domain.SaleRepository;
import com.tpay.domains.refund.application.dto.RefundFindResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefundFindService {
  private final SaleRepository saleRepository;

  @Transactional
  public RefundFindResponse findRefundOne(String orderNumber) {
    OrderEntity orderEntity = saleRepository.findByOrderNumber(orderNumber);
    String totalAmount =
        orderEntity.getOrderLineEntity().stream()
            .map(saleLineEntity -> Long.parseLong(saleLineEntity.getTotalPrice()))
            .reduce(Long::sum)
            .get()
            .toString();
    Long saleAmount = Long.parseLong(totalAmount) - Long.parseLong(orderEntity.getTotalVat());

    return RefundFindResponse.builder()
        .name(orderEntity.getCustomerEntity().getCustomerName())
        .nation(orderEntity.getCustomerEntity().getNation())
        .passportNumber(orderEntity.getCustomerEntity().getPassportNumber())
        .orderNumber(orderEntity.getOrderNumber())
        .saleDate(orderEntity.getCreatedDate().toString())
        .totalAmount(totalAmount)
        .totalVat(orderEntity.getTotalVat())
        .saleAmount(saleAmount.toString())
        .point("미정")
        .build();
  }
}
