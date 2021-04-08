package com.tpay.domains.refund.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.refund.application.dto.RefundCancelRequest;
import com.tpay.domains.refund.application.dto.RefundCancelResponse;
import com.tpay.domains.refund.application.dto.RefundResponse;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class RefundCancelService {

  WebClient webClient = WebClient.builder().baseUrl(CustomValue.REFUND_SERVER).build();

  private final CustomerRepository customerRepository;
  private final RefundRepository refundCancelRequest;

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
            .amount(refundEntity.getSaleEntity().getTotalAmount())
            .name(customerEntity.getCustomerName())
            .nationality(customerEntity.getNation())
            .orderNumber(refundEntity.getSaleEntity().getOrderNumber())
            .passportNumber(customerEntity.getPassportNumber())
            .purchaseSequenceNumber(
                "990" + refundEntity.getSaleEntity().getOrderNumber().substring(1))
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

    RefundCancelResponse refundCancelResponse =
        RefundCancelResponse.builder()
            .userIndex(customerEntity.getId())
            .refundResponse(refundResponse)
            .build();

    return refundCancelResponse;
  }
}
