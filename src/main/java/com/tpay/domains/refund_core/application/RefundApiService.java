package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.detail.KtpApiException;
import com.tpay.commons.exception.detail.WebfluxGeneralException;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.api.domain.vo.ApprovalDto;
import com.tpay.domains.customer.application.CustomerApiService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.application.RefundFindApiService;
import com.tpay.domains.refund.application.RefundService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund_core.application.dto.RefundApproveRequest;
import com.tpay.domains.refund_core.application.dto.RefundCancelRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class RefundApiService {

    private final CustomerApiService customerService;
    private final RefundFindApiService refundFindService;
    private final WebRequestUtil webRequestUtil;
    private final RefundRepository refundRepository;
    private final OrderSaveService orderSaveService;
    private final RefundService refundService;

    @Transactional
    public RefundResponse approve(Long customerIdx, ApprovalDto.Request request) {
        OrderEntity order = orderSaveService.save(customerIdx, request);
        RefundResponse refundResponse = requestRefundApprove(order);

        RefundEntity refundEntity =
                refundService.save(
                        refundResponse.getResponseCode(),
                        refundResponse.getPurchaseSequenceNumber(),
                        refundResponse.getTakeoutNumber(),
                        order);
        log.debug("Refund approve entity id = {} ", refundEntity.getId());

        return refundResponse;
    }

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

    private RefundResponse requestRefundApprove(OrderEntity order) {
        RefundApproveRequest refundApproveRequest = RefundApproveRequest.of(order);
        String uri = CustomValue.REFUND_SERVER + "/refund/approval";
        return requestToRefund(uri, refundApproveRequest);
    }

    private RefundResponse requestRefundCancel(CustomerEntity customerEntity, RefundEntity refundEntity) {
        RefundCancelRequest refundCancelRequest = RefundCancelRequest.of(customerEntity, refundEntity);
        String uri = CustomValue.REFUND_SERVER + "/refund/cancel";
        return requestToRefund(uri, refundCancelRequest);
    }

    private RefundResponse requestToRefund(String uri, Object requestData) {
        try {
            return webRequestUtil.post(uri, requestData);
        } catch (WebfluxGeneralException e) {
            String[] split = e.getMessage().split(":");
            throw new KtpApiException(split[0], split[1]);
        }
    }

    public int updateBulkRefundCancel(List<Long> ids) {
        return refundRepository.updateRefundCancel(ids);
    }
}
