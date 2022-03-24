package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionResponse;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.application.PointScheduledChangeService;
import com.tpay.domains.refund.application.RefundFindService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundCancelRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class RefundCancelService {

    private final CustomerFindService customerFindService;
    private final RefundFindService refundFindService;
    private final PointScheduledChangeService pointScheduledChangeService;
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
                .onStatus(
                    HttpStatus::isError,
                    response ->
                        response.bodyToMono(ExceptionResponse.class).flatMap(error -> Mono.error(new InvalidParameterException(
                            ExceptionState.REFUND, error.getMessage()))))
                .bodyToMono(RefundResponse.class)
                // .exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
                .block();

        refundEntity.updateCancel(refundResponse.getResponseCode());
        pointScheduledChangeService.change(refundEntity, SignType.NEGATIVE);

        return refundResponse;
    }
}
