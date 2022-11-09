package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerApiService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.refund.application.RefundFindApiService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundCancelRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefundCancelApiService {

    private final CustomerApiService customerService;
    private final RefundFindApiService refundFindService;
    private final WebRequestUtil webRequestUtil;

    @Transactional
    public RefundResponse cancel(Long customerIndex, Long refundIndex) {
        CustomerEntity customerEntity = customerService.findByIndex(customerIndex);
        RefundEntity refundEntity = refundFindService.findById(refundIndex);
        RefundCancelRequest refundCancelRequest = RefundCancelRequest.of(customerEntity, refundEntity);

        String uri = CustomValue.REFUND_SERVER + "/refund/cancel";
        RefundResponse refundResponse = webRequestUtil.post(uri, refundCancelRequest);

        if (refundResponse.getResponseCode().equals("0000")) {
            refundEntity.updateCancel();
        }
        return refundResponse;
    }
}
