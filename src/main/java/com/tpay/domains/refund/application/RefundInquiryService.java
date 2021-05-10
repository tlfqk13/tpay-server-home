package com.tpay.domains.refund.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.refund.application.dto.RefundInquiryResponse;
import com.tpay.domains.refund.application.dto.RefundResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class RefundInquiryService {

  private final CustomerRepository customerRepository;

  //WebClient webClient = WebClient.builder().baseUrl(CustomValue.REFUND_SERVER).build();
  WebClient webClient = WebClient.create("http://localhost:20001");

  @Transactional
  public RefundInquiryResponse refundInquiry(RefundInquiryRequest refundInquiryRequest) {

    RefundResponse refundResponse =
        webClient
            .post()
            .uri("/refund/limit/inquiry")
            .bodyValue(refundInquiryRequest)
            .exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
            .block();

    CustomerEntity customerEntity =
        customerRepository
            .findByCustomerNameAndPassportNumber(
                refundInquiryRequest.getName(), refundInquiryRequest.getPassportNumber())
            .orElseGet(
                () ->
                    customerRepository.save(
                        CustomerEntity.builder()
                            .customerName(refundInquiryRequest.getName())
                            .passportNumber(refundInquiryRequest.getPassportNumber())
                            .nation(refundInquiryRequest.getNationality())
                            .build()));

    RefundInquiryResponse refundInquiryResponse =
        RefundInquiryResponse.builder()
            .refundResponse(refundResponse)
            .userIndex(customerEntity.getId())
            .build();

    return refundInquiryResponse;
  }
}
