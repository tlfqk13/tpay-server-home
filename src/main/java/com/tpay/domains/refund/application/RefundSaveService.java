package com.tpay.domains.refund.application;

import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundSaveService {

  private final RefundRepository refundRepository;

  @Transactional
  public RefundEntity save(String responseCode, String orderNumber, String takeOutNumber, OrderEntity orderEntity) {
    RefundEntity refundEntity =
        RefundEntity.builder()
            .responseCode(responseCode)
            .orderNumber(orderNumber)
            .takeOutNumber(takeOutNumber)
            .orderEntity(orderEntity)
            .build();

    return refundRepository.save(refundEntity);
  }
}
