package com.tpay.domains.refund.application;

import com.tpay.domains.refund.application.dto.RefundFindResponse;
import com.tpay.domains.sale.domain.SaleEntity;
import com.tpay.domains.sale.domain.SaleRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefundFindService {
  private final SaleRepository saleRepository;

  @Transactional
  public RefundFindResponse findRefundOne(String orderNumber) {
    SaleEntity saleEntity = saleRepository.findByOrderNumber(orderNumber);
    String totalAmount = saleEntity.getSaleLineEntity().stream().map(saleLineEntity -> Long.parseLong(saleLineEntity.getTotalPrice())).reduce(Long::sum).get().toString();
    Long saleAmount = Long.parseLong(totalAmount) - Long.parseLong(saleEntity.getTotalVat());

    return RefundFindResponse.builder()
        .name(saleEntity.getCustomerEntity().getCustomerName())
        .nation(saleEntity.getCustomerEntity().getNation())
        .passportNumber(saleEntity.getCustomerEntity().getPassportNumber())
        .orderNumber(saleEntity.getOrderNumber())
        .saleDate(saleEntity.getCreatedDate().toString())
            .totalAmount(totalAmount)
            .totalVat(saleEntity.getTotalVat())
            .saleAmount(saleAmount.toString())
            .point("미정")
        .build();
  }
}
