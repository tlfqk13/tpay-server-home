package com.tpay.commons.webClient;


import com.tpay.commons.exception.ExceptionResponse;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.WebfluxGeneralException;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Webflux post, get Util 화 하여 공통적용하는 클래스
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class WebRequestUtil {

    private final WebClient.Builder builder;

    public RefundResponse post(String uri, Object request) {

        WebClient webClient = builder.build();
        return webClient
            .post()
            .uri(uri)
            .bodyValue(request)
            .retrieve()
            .onStatus(HttpStatus::isError, message -> message.bodyToMono(ExceptionResponse.class)
                .flatMap(error -> Mono.error(new WebfluxGeneralException(ExceptionState.WEBFLUX_GENERAL, error.getMessage()))))
            .bodyToMono(RefundResponse.class)
            .block();
    }

}
