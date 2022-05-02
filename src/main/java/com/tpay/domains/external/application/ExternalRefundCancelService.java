package com.tpay.domains.external.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.AlreadyCancelledException;
import com.tpay.commons.exception.detail.InvalidExternalRefundIndexException;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.external.application.dto.ExternalRefundCancelRequest;
import com.tpay.domains.external.application.dto.ExternalRefundResponse;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.application.PointScheduledChangeService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundCancelRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalRefundCancelService {

    private final ExternalRefundFindService externalRefundFindService;
    private final CustomerFindService customerFindService;
    private final PointScheduledChangeService pointScheduledChangeService;
    private final WebRequestUtil webRequestUtil;
    private final ObjectMapper objectMapper;

    public ExternalRefundResponse cancel(ExternalRefundCancelRequest externalRefundCancelRequest) {
        try {
            ExternalRefundEntity externalRefundEntity = externalRefundFindService.findById(externalRefundCancelRequest.getExternalRefundIndex());

            if (!externalRefundEntity.getExternalRefundStatus().equals(ExternalRefundStatus.APPROVE)) {
                throw new AlreadyCancelledException(ExceptionState.ALREADY_CANCELLED, "already cancelled or Illegal request");
            }

            CustomerEntity customerEntity = customerFindService.findByIndex(externalRefundEntity.getCustomerIndex());
            RefundEntity refundEntity = externalRefundEntity.getRefundEntity();
            RefundCancelRequest refundCancelRequest = RefundCancelRequest.of(customerEntity, refundEntity);

            String uri = CustomValue.REFUND_SERVER + "/refund/cancel";
            Object post = webRequestUtil.post(uri, refundCancelRequest);
            RefundResponse refundResponse = objectMapper.convertValue(post, RefundResponse.class);

            //0000이 아닌경우 에러 발생
            if (!refundResponse.getResponseCode().equals("0000")) {
                throw new Exception("Refund-Server Error");
            }

            refundEntity.updateCancel(refundResponse.getResponseCode());
            externalRefundEntity.changeStatus(ExternalRefundStatus.CANCEL);
            pointScheduledChangeService.change(refundEntity, SignType.NEGATIVE);


            ExternalRefundResponse externalRefundResponse = ExternalRefundResponse.builder()
                .responseCode(refundResponse.getResponseCode())
                .message(refundResponse.getMessage())
                .build();
            return externalRefundResponse;
        } catch (AlreadyCancelledException e) {
            return ExternalRefundResponse.builder().responseCode("9001").message("[User] 취소 가능한 상태가 아닙니다.").build();
        } catch (InvalidExternalRefundIndexException e) {
            return ExternalRefundResponse.builder().responseCode("9000").message("[User] Index 정보를 찾을 수 없습니다.").build();
        } catch (InvalidParameterException e) {
            return ExternalRefundResponse.builder().responseCode("9102").message("[successmode] Index 정보를 찾을 수 없습니다.").build();
        } catch (IllegalArgumentException e) {
            return ExternalRefundResponse.builder().responseCode("9101").message("[successmode] 내부 에러입니다.").build();
        } catch (Exception e) {
            return ExternalRefundResponse.builder().responseCode("9100").message("[successmode] Unknown 에러입니다.").build();
        }
    }
}
