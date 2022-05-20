package com.tpay.domains.external.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.detail.InvalidExternalRefundIndexException;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.external.application.dto.ExternalRefundApprovalRequest;
import com.tpay.domains.external.application.dto.ExternalRefundResponse;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.application.OrderService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.application.PointScheduledChangeService;
import com.tpay.domains.push.application.NonBatchPushService;
import com.tpay.domains.refund.application.RefundService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundApproveRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalRefundApprovalService {


    private final FranchiseeFindService franchiseeFindService;
    private final WebRequestUtil webRequestUtil;
    private final ObjectMapper objectMapper;
    private final RefundService refundService;
    private final PointScheduledChangeService pointScheduledChangeService;
    private final ExternalRefundFindService externalRefundFindService;
    private final NonBatchPushService nonBatchPushService;
    private final PaymentCalculator paymentCalculator;
    private final OrderSaveService orderSaveService;
    private final OrderService orderService;
    private final S3FileUploader s3FileUploader;

    @Transactional
    public ExternalRefundResponse approve(ExternalRefundApprovalRequest externalRefundApprovalRequest) {

        // 1. 금액이 정상 범위인지
        int amount = Integer.parseInt(externalRefundApprovalRequest.getAmount());
        if (amount >= 500000 || amount < 30000) {
            log.error("CODE[K8500] - externalRefundIndex : {}, amount : {}", externalRefundApprovalRequest.getExternalRefundIndex(), amount);
            return ExternalRefundResponse.builder().responseCode("K8500").message("[K8500] 환급 가능 범위는 3만원 이상 50만원 미만입니다.").build();
        }

        try {
            s3FileUploader.deleteBarcode(externalRefundApprovalRequest.getExternalRefundIndex());
            ExternalRefundEntity externalRefundEntity = externalRefundFindService.findById(externalRefundApprovalRequest.getExternalRefundIndex());
            FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(externalRefundEntity.getFranchiseeIndex());

            if (!externalRefundEntity.getExternalRefundStatus().equals(ExternalRefundStatus.SCAN)) {
                log.error("CODE[K8101] - externalRefundIndex : {}, 환급 시도 INDEX가 SCAN 상태가 아님", externalRefundApprovalRequest.getExternalRefundIndex());
                return ExternalRefundResponse.builder().responseCode("K8101").message("[K8101] 만료된 거래입니다.").build();
            }

            OrderEntity orderEntity = orderSaveService.save(externalRefundEntity, externalRefundApprovalRequest.getAmount());
            log.trace("CODE[K5000] - externalRefundIndex : {}, orderIndex : {} successfully SAVED", externalRefundEntity.getId(), orderEntity.getId());
            RefundApproveRequest refundApproveRequest = RefundApproveRequest.of(orderEntity);

            String uri = CustomValue.REFUND_SERVER + "/refund/approval";
            Object post = webRequestUtil.post(uri, refundApproveRequest);
            RefundResponse refundResponse = objectMapper.convertValue(post, RefundResponse.class);

            //0000이 아닌경우 에러 발생
            if (!refundResponse.getResponseCode().equals("0000")) {
                log.error("CODE[R8102] - externalRefundIndex : {}, 관세청 응답메시지 : {}", externalRefundApprovalRequest.getExternalRefundIndex(), refundResponse.getMessage());
                orderService.deleteByIndex(orderEntity.getId());
                return ExternalRefundResponse.builder().responseCode("R8102").message("[R8102] 시스템 에러입니다.").build();
            }

            RefundEntity refundEntity = refundService.save(
                refundResponse.getResponseCode(),
                refundResponse.getPurchaseSequenceNumber(),
                refundResponse.getTakeoutNumber(),
                orderEntity);
            log.trace("CODE[K5001] - externalRefundIndex : {}, refundIndex : {} successfully SAVED", externalRefundEntity.getId(), refundEntity.getId());

            Integer payment = paymentCalculator.paymentInteger(refundEntity);
            pointScheduledChangeService.change(refundEntity, SignType.POSITIVE);
            externalRefundEntity.refundIndexRegister(refundEntity);
            externalRefundEntity.changeStatus(ExternalRefundStatus.APPROVE);

            if (!franchiseeEntity.getIsRefundOnce()) {
                nonBatchPushService.nonBatchPushNSave(PushCategoryType.CASE_FIVE, franchiseeEntity.getId());
                franchiseeEntity.isRefundOnce();
            }
            ExternalRefundResponse externalRefundResponse = ExternalRefundResponse.builder()
                .responseCode(refundResponse.getResponseCode())
                .message(refundResponse.getMessage())
                .payment(payment)
                .build();
            log.trace("CODE[K5002] - externalRefundIndex : {} successfully changed to APPROVED", externalRefundEntity.getId());
            return externalRefundResponse;
        } catch (InvalidExternalRefundIndexException e) {
            log.error("CODE[K8103] - externalRefundIndex : {}, externalRefundIndex를 찾을 수 없음", externalRefundApprovalRequest.getExternalRefundIndex());
            return ExternalRefundResponse.builder().responseCode("K8103").message("[K8103] 시스템 에러입니다.").build();
        } catch (InvalidParameterException e) {
            log.error("CODE[K8104] - externalRefundIndex : {}", externalRefundApprovalRequest.getExternalRefundIndex());
            return ExternalRefundResponse.builder().responseCode("K8104").message("[K8104] 시스템 에러입니다.").build();
        } catch (IllegalArgumentException e) {
            log.error("CODE[K8105] - externalRefundIndex : {}", externalRefundApprovalRequest.getExternalRefundIndex());
            return ExternalRefundResponse.builder().responseCode("K8105").message("[K8105] 시스템 에러입니다.").build();
        } catch (Exception e) {
            log.error("CODE[K8106] - externalRefundIndex : {}", externalRefundApprovalRequest.getExternalRefundIndex());
            return ExternalRefundResponse.builder().responseCode("K8106").message("[K8106] 시스템 에러입니다.").build();
        }

    }

}
