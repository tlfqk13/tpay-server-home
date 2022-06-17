package com.tpay.domains.refund_core.application;

import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerFindService;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefundCancelService {

    private final CustomerFindService customerFindService;
    private final RefundFindService refundFindService;
    private final PointScheduledChangeService pointScheduledChangeService;
    private final ExternalRepository externalRepository;
    private final WebRequestUtil webRequestUtil;

    @Transactional
    public RefundResponse cancel(Long customerIndex, Long refundIndex) {
        CustomerEntity customerEntity = customerFindService.findByIndex(customerIndex);
        RefundEntity refundEntity = refundFindService.findById(refundIndex);
        RefundCancelRequest refundCancelRequest = RefundCancelRequest.of(customerEntity, refundEntity);

        //2022/03/25 포스기에서 승인한건 포스기에서만 취소 가능
        Optional<ExternalRefundEntity> optionalExternalRefundEntity = externalRepository.findByRefundEntity(refundEntity);
        if (optionalExternalRefundEntity.isPresent()) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "POS approval must be canceled by POS");
        }

        String uri = CustomValue.REFUND_SERVER + "/refund/cancel";
        RefundResponse refundResponse = webRequestUtil.post(uri, refundCancelRequest);


        refundEntity.updateCancel(refundResponse.getResponseCode());

        Integer balancePercentage = refundEntity.getOrderEntity().getFranchiseeEntity().getBalancePercentage();
        pointScheduledChangeService.change(refundEntity, SignType.NEGATIVE, balancePercentage);

        return refundResponse;
    }
}
