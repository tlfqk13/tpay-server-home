package com.tpay.domains.refund.application;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.refund.application.dto.RefundCancelRequest;
import com.tpay.domains.refund.application.dto.RefundCancelResponse;
import com.tpay.domains.refund.application.dto.RefundResponse;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.order.domain.OrderEntity;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class RefundCancelService {

  //WebClient webClient = WebClient.builder().baseUrl(CustomValue.REFUND_SERVER).build();
  WebClient webClient = WebClient.create("http://localhost:20001");

  private final CustomerRepository customerRepository;
  private final RefundRepository refundCancelRequest;
  private final PointRepository pointRepository;

  @Transactional
  public RefundCancelResponse refundCancel(Long userIndex, Long refundIndex) {
    CustomerEntity customerEntity =
        customerRepository
            .findById(userIndex)
            .orElseThrow(() -> new IllegalArgumentException("Invalid User Index"));
    RefundEntity refundEntity =
        refundCancelRequest
            .findById(refundIndex)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Refund Index"));

    RefundCancelRequest refundCancelRequest =
        RefundCancelRequest.builder()
            .amount(refundEntity.getOrderEntity().getTotalAmount())
            .name(customerEntity.getCustomerName())
            .nationality(customerEntity.getNation())
            .orderNumber(refundEntity.getOrderEntity().getOrderNumber())
            .passportNumber(customerEntity.getPassportNumber())
            .purchaseSequenceNumber(
                "990" + refundEntity.getOrderEntity().getOrderNumber().substring(1))
            .takeOutNumber(refundEntity.getApprovalNumber())
            .build();

    RefundResponse refundResponse =
        webClient
            .post()
            .uri("/refund/cancellation")
            .bodyValue(refundCancelRequest)
            .exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
            .block();

    refundEntity.updateCancel(refundResponse.getResponseCode());

    OrderEntity orderEntity = refundEntity.getOrderEntity();
    FranchiseeEntity franchiseeEntity = orderEntity.getFranchiseeEntity();
    long point = (long) Math.floor(Double.parseDouble(orderEntity.getTotalAmount()) * 7) / 100;
    franchiseeEntity.changeBalance(SignType.NEGATIVE, point);
    PointEntity pointEntity =
        PointEntity.builder()
            .createdDate(LocalDateTime.now())
            .signType(SignType.NEGATIVE)
            .change(point)
            .pointStatus(PointStatus.CANCEL)
            .balance(franchiseeEntity.getBalance())
            .franchiseeEntity(franchiseeEntity)
            .build();
    pointRepository.save(pointEntity);

    RefundCancelResponse refundCancelResponse =
        RefundCancelResponse.builder()
            .userIndex(customerEntity.getId())
            .refundResponse(refundResponse)
            .build();

    return refundCancelResponse;
  }
}
