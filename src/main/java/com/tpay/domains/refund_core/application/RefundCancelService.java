package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRepository;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.application.PointScheduledChangeService;
import com.tpay.domains.refund.application.RefundFindService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundCancelRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class RefundCancelService {

    private final CustomerService customerService;
    private final RefundFindService refundFindService;
    private final PointScheduledChangeService pointScheduledChangeService;
    private final ExternalRepository externalRepository;
    private final WebRequestUtil webRequestUtil;

    @Transactional
    public RefundResponse cancel(Long customerIndex, Long refundIndex) {
        CustomerEntity customerEntity = customerService.findByIndex(customerIndex);
        RefundEntity refundEntity = refundFindService.findById(refundIndex);
        RefundCancelRequest refundCancelRequest = RefundCancelRequest.of(customerEntity, refundEntity);
        RefundResponse refundResponse = null;

        // 포스기에서 승인한건 포스기에서만 취소 가능 -- external index 확인
        Optional<ExternalRefundEntity> optionalExternalRefundEntity = externalRepository.findByRefundEntity(refundEntity);
        if (optionalExternalRefundEntity.isPresent()) {
            log.trace(" @@ optionalExternalRefundEntity = {}", refundIndex);
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "POS approval must be canceled by POS");
        }

        if (refundEntity.getRefundAfterEntity().getId() != null) {
            this.cancelRefundAfter(refundIndex);
            log.trace(" 사후환급 취소 " );
            refundResponse = RefundResponse.builder()
                    .responseCode("0000")
                    .build();
        } else {

            String uri = CustomValue.REFUND_SERVER + "/refund/cancel";
            refundResponse = webRequestUtil.post(uri, refundCancelRequest);

            if (refundResponse.getResponseCode().equals("0000")) {
                refundEntity.updateCancel();
            }
            double balancePercentage = refundEntity.getOrderEntity().getFranchiseeEntity().getBalancePercentage();
            pointScheduledChangeService.change(refundEntity, SignType.NEGATIVE, balancePercentage);
        }

        log.warn(" @@ refundResponse = {}", refundResponse.getResponseCode());

        return refundResponse;
    }

    @org.springframework.transaction.annotation.Transactional
    public void cancelRefundAfter(String tkOutNumber) {
        RefundEntity refund = refundFindService.getRefundByTkOutNumber(tkOutNumber);
        refund.updateCancel();
    }

    public void cancelRefundAfter(Long refundIndex) {
        RefundEntity refund = refundFindService.getRefundByRefundId(refundIndex);
        double balancePercentage = refund.getOrderEntity().getFranchiseeEntity().getBalancePercentage();
        pointScheduledChangeService.change(refund, SignType.NEGATIVE, balancePercentage);
        refund.updateCancel();
    }
}
