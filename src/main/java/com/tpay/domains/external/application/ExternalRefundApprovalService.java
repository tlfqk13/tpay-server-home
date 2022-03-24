package com.tpay.domains.external.application;


import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionResponse;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.external.application.dto.ExternalRefundApprovalRequest;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.application.PointScheduledChangeService;
import com.tpay.domains.refund.application.RefundSaveService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundApproveRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ExternalRefundApprovalService {

  private final OrderSaveService orderSaveService;
  private final FranchiseeFindService franchiseeFindService;
  private final WebClient.Builder builder;
  private final RefundSaveService refundSaveService;
  private final PointScheduledChangeService pointScheduledChangeService;
  private final ExternalRefundFindService externalRefundFindService;

  @Transactional
  public RefundResponse approve(ExternalRefundApprovalRequest externalRefundApprovalRequest) {
    ExternalRefundEntity externalRefundEntity = externalRefundFindService.findById(externalRefundApprovalRequest.getExternalRefundIndex());
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(externalRefundEntity.getFranchiseeIndex());
    OrderEntity orderEntity = orderSaveService.save(externalRefundEntity, externalRefundApprovalRequest.getAmount());
    externalRefundEntity.changeStatus(ExternalRefundStatus.APPROVE);

    RefundApproveRequest refundApproveRequest = RefundApproveRequest.of(orderEntity);

    WebClient webClient = builder.build();
    String uri = CustomValue.REFUND_SERVER + "/refund/approval";
    RefundResponse refundResponse = webClient
        .post()
        .uri(uri)
        .bodyValue(refundApproveRequest)
        .retrieve()
        .onStatus(
            HttpStatus::isError,
            response ->
                response.bodyToMono(ExceptionResponse.class).flatMap(error -> Mono.error(new InvalidParameterException(
                    ExceptionState.REFUND, error.getMessage()))))
        .bodyToMono(RefundResponse.class)
        .block();

    RefundEntity refundEntity = refundSaveService.save(
        refundResponse.getResponseCode(),
        refundResponse.getPurchaseSequenceNumber(),
        refundResponse.getTakeoutNumber(),
        orderEntity);

    pointScheduledChangeService.change(refundEntity, SignType.POSITIVE);
    franchiseeEntity.isRefundOnce();
    return refundResponse;


  }

}
