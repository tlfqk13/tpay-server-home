package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionResponse;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.webClient.WebRequestUtil;
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
    private final WebRequestUtil webRequestUtil;

    @Transactional
    public RefundResponse find(RefundLimitRequest request) {
        String uri = CustomValue.REFUND_SERVER + "/refund/limit";

        CustomerEntity customerEntity =
            customerFindService.findByNationAndPassportNumber(request.getName(), request.getPassportNumber(), request.getNationality());
        RefundResponse refundResponse = webRequestUtil.post(uri, request);
        return refundResponse.addCustomerInfo(customerEntity.getId());
    }
}
