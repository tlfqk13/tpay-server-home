package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerUpdateService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.refund_core.application.dto.RefundLimitRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LimitFindService {

    private final CustomerUpdateService customerUpdateService;
    private final WebRequestUtil webRequestUtil;

    @Transactional
    public RefundResponse find(RefundLimitRequest request) {
        String uri = CustomValue.REFUND_SERVER + "/refund/limit";

        Long customerEntityId;
        Optional<CustomerEntity> customerEntityOptional = customerUpdateService.findCustomerByNationAndPassportNumber(request.getPassportNumber(), request.getNationality());
        if(customerEntityOptional.isEmpty()) {
            CustomerEntity customerEntity = customerUpdateService.updateCustomerInfo(request.getName(), request.getPassportNumber(), request.getNationality());
            customerEntityId = customerEntity.getId();
        } else {
            log.trace("Customer already exists.");
            customerEntityId = customerEntityOptional.get().getId();
        }
        log.trace("limit request = {} ", request);
        RefundResponse refundResponse = webRequestUtil.post(uri, request);
        log.trace("Limit find response = {}, customer id = {} ", refundResponse, customerEntityId);
        return refundResponse.addCustomerInfo(customerEntityId);
    }
}
