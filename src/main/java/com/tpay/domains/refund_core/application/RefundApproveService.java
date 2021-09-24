package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.application.PointSaveService;
import com.tpay.domains.point.domain.PointRepository;
import com.tpay.domains.refund.application.RefundSaveService;
import com.tpay.domains.refund.application.dto.RefundSaveRequest;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundApproveRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class RefundApproveService {

  private final OrderSaveService orderSaveService;
  private final RefundSaveService refundSaveService;
  private final PointSaveService pointSaveService;
  private final WebClient.Builder builder;

  private final PointRepository pointRepository;

  @Transactional
  public RefundResponse approve(RefundSaveRequest request) {
    OrderEntity orderEntity = orderSaveService.save(request);
    RefundApproveRequest refundApproveRequest = RefundApproveRequest.of(orderEntity);

    WebClient webClient = builder.build();
    String uri = CustomValue.REFUND_SERVER + "/refund/approval";
    RefundResponse refundResponse =
        webClient
            .post()
            .uri(uri)
            .bodyValue(refundApproveRequest)
            .retrieve()
            .bodyToMono(RefundResponse.class)
            // .exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
            .block();

    // TODO : 응답코드 "0000" 아닐시 테스트 필요
    RefundEntity refundEntity =
        refundSaveService.save(
            refundResponse.getResponseCode(),
            refundResponse.getPurchaseSequenceNumber(),
            refundResponse.getTakeoutNumber(),
            orderEntity);

    pointSaveService.save(refundEntity);

    return refundResponse;
  }
}
