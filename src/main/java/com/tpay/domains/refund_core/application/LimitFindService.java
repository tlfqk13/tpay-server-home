package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class LimitFindService {

  private final CustomerFindService customerFindService;
  private final WebClient.Builder builder;

  @Transactional
  public RefundResponse find(RefundLimitRequest request) {
    WebClient webClient = builder.build();
    String uri = CustomValue.REFUND_SERVER + "/refund/limit";
    RefundResponse refundResponse =
        webClient
            .post()
            .uri(uri)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(RefundResponse.class)
            // .exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
            .block();

    CustomerEntity customerEntity =
        customerFindService.findByCustomerNameAndPassportNumber(
            request.getName(), request.getPassportNumber(), request.getNationality());

    return refundResponse;
  }
}
