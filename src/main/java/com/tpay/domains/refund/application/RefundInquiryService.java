package com.tpay.domains.refund.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.refund.application.dto.RefundInquiryRequest;
import com.tpay.domains.refund.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class RefundInquiryService {

  WebClient webClient = WebClient.builder().baseUrl(CustomValue.REFUND_SERVER).build();

  public RefundResponse refundInquiry(RefundInquiryRequest refundInquiryRequest) {
    RefundResponse refundResponse =
        webClient
            .post()
            .uri("/refund/limit/inquiry")
            .bodyValue(refundInquiryRequest)
            .exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
            .block();
    return refundResponse;
  }
}
