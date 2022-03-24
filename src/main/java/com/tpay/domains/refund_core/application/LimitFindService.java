package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionResponse;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LimitFindService {

    private final CustomerFindService customerFindService;
    private final WebClient.Builder builder;

    @Transactional
    public RefundResponse find(RefundLimitRequest request) {
        WebClient webClient = builder.build();
        String uri = CustomValue.REFUND_SERVER + "/refund/limit";

        CustomerEntity customerEntity =
            customerFindService.findByNationAndPassportNumber(request.getName(), request.getPassportNumber(), request.getNationality());

        RefundResponse refundResponse =
            webClient
                .post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                    HttpStatus::isError,
                    response ->
                        response.bodyToMono(ExceptionResponse.class).flatMap(error -> Mono.error(new InvalidParameterException(
                            ExceptionState.REFUND, error.getMessage()))))
                .bodyToMono(RefundResponse.class)
                // .exchangeToMono(clientResponse -> clientResponse.bodyToMono(RefundResponse.class))
                .block();

        return refundResponse.addCustomerInfo(customerEntity.getId());
    }
}
