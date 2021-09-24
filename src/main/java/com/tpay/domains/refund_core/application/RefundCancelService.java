package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.point.application.PointCancelService;
import com.tpay.domains.refund.application.RefundFindService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundCancelRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class RefundCancelService {

  private final CustomerFindService customerFindService;
  private final RefundFindService refundFindService;
  private final PointCancelService pointCancelService;
  private final WebClient.Builder builder;

  @Transactional
  public RefundResponse cancel(Long customerIndex, Long refundIndex) {
    CustomerEntity customerEntity = customerFindService.findByIndex(customerIndex);
    RefundEntity refundEntity = refundFindService.findByIndex(refundIndex);
    RefundCancelRequest refundCancelRequest = RefundCancelRequest.of(customerEntity, refundEntity);

    WebClient webClient = builder.build();
    String uri = CustomValue.REFUND_SERVER + "/refund/cancel";
    RefundResponse refundResponse =
        webClient
            .post()
            .uri(uri)
            .bodyValue(refundCancelRequest)
            .retrieve()
            .bodyToMono(RefundResponse.class)
            // .exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
            .block();

    refundEntity.updateCancel(refundResponse.getResponseCode());
    pointCancelService.cancel(refundEntity);

    return refundResponse;
  }
}
