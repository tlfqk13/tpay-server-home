package com.tpay.domains.external.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.custom.CustomValue;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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

            ExternalRefundStatus externalRefundStatus = externalRefundEntity.getExternalRefundStatus();
            if (!(externalRefundStatus.equals(ExternalRefundStatus.APPROVE) || externalRefundStatus.equals(ExternalRefundStatus.CONFIRMED))) {
                log.error("CODE[K9100] - externalRefundIndex : {}, 이미 취소된 건입니다.", externalRefundCancelRequest.getExternalRefundIndex());
                return ExternalRefundResponse.builder().responseCode("K9100").payment(0).message("[K9100] 이미 취소된 건입니다.").build();
            }

            CustomerEntity customerEntity = customerFindService.findByIndex(externalRefundEntity.getCustomerIndex());
            RefundEntity refundEntity = externalRefundEntity.getRefundEntity();
            RefundCancelRequest refundCancelRequest = RefundCancelRequest.of(customerEntity, refundEntity);

            String uri = CustomValue.REFUND_SERVER + "/refund/cancel";
            Object post = webRequestUtil.post(uri, refundCancelRequest);
            RefundResponse refundResponse = objectMapper.convertValue(post, RefundResponse.class);

            //0000이 아닌경우 에러 발생
            if (!refundResponse.getResponseCode().equals("0000")) {
                log.error("CODE[R9101] - externalRefundIndex : {}, 관세청 응답메시지 : {}", externalRefundCancelRequest.getExternalRefundIndex(), refundResponse.getMessage());
                return ExternalRefundResponse.builder().responseCode("R9105").message("[R9101] 시스템 에러입니다.").build();
            }

            refundEntity.updateCancel(refundResponse.getResponseCode());
            externalRefundEntity.changeStatus(ExternalRefundStatus.CANCEL);
            pointScheduledChangeService.change(refundEntity, SignType.NEGATIVE);

            return ExternalRefundResponse.builder()
                .responseCode(refundResponse.getResponseCode())
                .payment(0)
                .message(refundResponse.getMessage())
                .build();
        } catch (InvalidExternalRefundIndexException e) {
            log.error("CODE[K9101] - externalRefundIndex : {}, externalRefundIndex를 찾을 수 없음", externalRefundCancelRequest.getExternalRefundIndex());
            return ExternalRefundResponse.builder().responseCode("K9101").payment(0).message("[K9101] 시스템 에러입니다.").build();
        } catch (InvalidParameterException e) {
            log.error("CODE[K9102] - externalRefundIndex : {}", externalRefundCancelRequest.getExternalRefundIndex());
            return ExternalRefundResponse.builder().responseCode("K9102").payment(0).message("[K9102] 시스템 에러입니다.").build();
        } catch (IllegalArgumentException e) {
            log.error("CODE[K9103] - externalRefundIndex : {}", externalRefundCancelRequest.getExternalRefundIndex());
            return ExternalRefundResponse.builder().responseCode("K9103").payment(0).message("[K9103] 시스템 에러입니다.").build();
        } catch (Exception e) {
            log.error("CODE[K9104] - externalRefundIndex : {}", externalRefundCancelRequest.getExternalRefundIndex());
            return ExternalRefundResponse.builder().responseCode("K9104").payment(0).message("[K9104] 시스템 에러입니다.").build();
        }
    }
}
