package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerApiService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.refund.application.RefundFindApiService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund_core.application.dto.RefundCancelRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RefundCancelApiService {

    private final CustomerApiService customerService;
    private final RefundFindApiService refundFindService;
    private final WebRequestUtil webRequestUtil;
    private final RefundRepository refundRepository;

    @Transactional
    public RefundResponse cancel(Long customerIndex, Long refundIndex) {
        CustomerEntity customerEntity = customerService.findByIndex(customerIndex);
        RefundEntity refundEntity = refundFindService.findById(refundIndex);
        RefundResponse refundResponse = requestRefundCancel(customerEntity, refundEntity);

        if (refundResponse.getResponseCode().equals("0000")) {
            refundEntity.updateCancel();
        }
        return refundResponse;
    }

    @Transactional
    public String cancelBulk(Long customerIndex, Long refundIndex) {
        CustomerEntity customerEntity = customerService.findByIndex(customerIndex);
        RefundEntity refundEntity = refundFindService.findById(refundIndex);
        RefundResponse refundResponse = requestRefundCancel(customerEntity, refundEntity);

        return refundResponse.getResponseCode();
    }

    private RefundResponse requestRefundCancel(CustomerEntity customerEntity, RefundEntity refundEntity) {
        RefundCancelRequest refundCancelRequest = RefundCancelRequest.of(customerEntity, refundEntity);

        String uri = CustomValue.REFUND_SERVER + "/refund/cancel";
        RefundResponse refundResponse = webRequestUtil.post(uri, refundCancelRequest);
        return refundResponse;
    }

    public int updateBulkRefundCancel(List<Long> ids) {
        return refundRepository.updateRefundCancel(ids);
    }
}
