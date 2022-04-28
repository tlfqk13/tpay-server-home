package com.tpay.domains.external.application;


import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.ExceptionResponse;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidExternalRefundIndexException;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.push.PushCategoryType;
import com.tpay.domains.external.application.dto.ExternalRefundApprovalRequest;
import com.tpay.domains.external.application.dto.ExternalRefundResponse;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.application.OrderSaveService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.application.PointScheduledChangeService;
import com.tpay.domains.push.application.NonBatchPushService;
import com.tpay.domains.refund.application.RefundService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundApproveRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ExternalRefundApprovalService {

    private final OrderSaveService orderSaveService;
    private final FranchiseeFindService franchiseeFindService;
    private final WebClient.Builder builder;
    private final RefundService refundService;
    private final PointScheduledChangeService pointScheduledChangeService;
    private final ExternalRefundFindService externalRefundFindService;
    private final NonBatchPushService nonBatchPushService;

    @Transactional
    public ExternalRefundResponse approve(ExternalRefundApprovalRequest externalRefundApprovalRequest) {
        int amount = Integer.parseInt(externalRefundApprovalRequest.getAmount());
        if (amount >= 500000 || amount < 30000) {
            return ExternalRefundResponse.builder().responseCode("8001").message("[User] 환급 금액이 범위 밖입니다. (환급 금액은 3만원 이상 50만원 미만입니다.)").build();
        }
        try {
            ExternalRefundEntity externalRefundEntity = externalRefundFindService.findById(externalRefundApprovalRequest.getExternalRefundIndex());

            if (!externalRefundEntity.getExternalRefundStatus().equals(ExternalRefundStatus.SCAN)) {
                return ExternalRefundResponse.builder().responseCode("8002").message("[User] 이미 종료된 Index입니다.").build();
            }

            FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(externalRefundEntity.getFranchiseeIndex());

            OrderEntity orderEntity = orderSaveService.save(externalRefundEntity, externalRefundApprovalRequest.getAmount());
            externalRefundEntity.changeStatus(ExternalRefundStatus.APPROVE);

            RefundApproveRequest refundApproveRequest = RefundApproveRequest.of(orderEntity);

            WebClient webClient = builder.build();
            String uri = CustomValue.REFUND_SERVER + "/refund/approval";
            RefundResponse refundResponse = webClient
                .post()
                .uri(uri)
                .bodyValue(refundApproveRequest)
                .retrieve()
                .onStatus(
                    HttpStatus::isError,
                    response ->
                        response.bodyToMono(ExceptionResponse.class).flatMap(error -> Mono.error(new InvalidParameterException(
                            ExceptionState.REFUND, error.getMessage()))))
                .bodyToMono(RefundResponse.class)
                .block();

            //0000이 아닌경우 에러 발생
            if (!refundResponse.getResponseCode().equals("0000")) {
                return ExternalRefundResponse.builder().responseCode("8103").message("[successmode] 관세청 에러입니다.").build();
            }

            RefundEntity refundEntity = refundService.save(
                refundResponse.getResponseCode(),
                refundResponse.getPurchaseSequenceNumber(),
                refundResponse.getTakeoutNumber(),
                orderEntity);

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
                .build();

            return externalRefundResponse;
        } catch (InvalidExternalRefundIndexException e) {
            return ExternalRefundResponse.builder().responseCode("8000").message("[User] Index 정보를 찾을 수 없습니다.").build();
        } catch (InvalidParameterException e) {
            return ExternalRefundResponse.builder().responseCode("8102").message("[successmode] Index 정보를 찾을 수 없습니다.").build();
        } catch (IllegalArgumentException e) {
            return ExternalRefundResponse.builder().responseCode("8101").message("[successmode] 내부 에러입니다.").build();
        } catch (Exception e) {
            return ExternalRefundResponse.builder().responseCode("8100").message("[successmode] Unknown 에러입니다.").build();
        }

    }

}
