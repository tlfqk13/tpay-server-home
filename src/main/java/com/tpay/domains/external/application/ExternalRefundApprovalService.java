package com.tpay.domains.external.application;


import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.detail.InvalidExternalRefundIndexException;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.WebfluxGeneralException;
import com.tpay.commons.logger.CommonLogger;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ExternalRefundApprovalService {


    private final FranchiseeFindService franchiseeFindService;
    private final OrderService orderService;
    private final OrderSaveService orderSaveService;
    private final RefundService refundService;
    private final PointScheduledChangeService pointScheduledChangeService;
    private final ExternalRefundFindService externalRefundFindService;
    private final NonBatchPushService nonBatchPushService;

    private final WebRequestUtil webRequestUtil;
    private final PaymentCalculator paymentCalculator;
    private final S3FileUploader s3FileUploader;
    private final CommonLogger commonLogger;

    @Transactional
    public ExternalRefundResponse approve(ExternalRefundApprovalRequest externalRefundApprovalRequest) {

        try {
            Long externalRefundIndex = externalRefundApprovalRequest.getExternalRefundIndex();
            commonLogger.headline(externalRefundIndex, "외부 환급 승인");
            // 1. 금액이 정상 범위인지
            int amount = Integer.parseInt(externalRefundApprovalRequest.getAmount());
            if (amount >= 500000 || amount < 30000) {
                commonLogger.error1(externalRefundIndex, "K8500", "환급 금액 벗어남. 입력값 : " + amount);
                return ExternalRefundResponse.builder().responseCode("K8500").message("[K8500] 환급 가능 범위는 3만원 이상 50만원 미만입니다.").build();
            }

            s3FileUploader.deleteBarcode(externalRefundApprovalRequest.getExternalRefundIndex());
            ExternalRefundEntity externalRefundEntity = externalRefundFindService.findById(externalRefundApprovalRequest.getExternalRefundIndex());
            FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(externalRefundEntity.getFranchiseeIndex());

            if (!externalRefundEntity.getExternalRefundStatus().equals(ExternalRefundStatus.SCAN)) {
                commonLogger.error1(externalRefundIndex, "K8101", "SCAN 상태가 아님. 입력값 : " + externalRefundEntity.getExternalRefundStatus());
                return ExternalRefundResponse.builder().responseCode("K8101").message("[K8101] 만료된 거래입니다.").build();
            }

            OrderEntity orderEntity = orderSaveService.save(externalRefundEntity, externalRefundApprovalRequest.getAmount());
            RefundApproveRequest refundApproveRequest = RefundApproveRequest.of(orderEntity);

            commonLogger.beforeHttpClient(externalRefundIndex);
            String uri = CustomValue.REFUND_SERVER + "/refund/approval";
            RefundResponse refundResponse = webRequestUtil.post(uri, refundApproveRequest);
            commonLogger.afterHttpClient(externalRefundIndex);

            //0000이 아닌경우 에러 발생
            if (!refundResponse.getResponseCode().equals("0000")) {
                commonLogger.error1(externalRefundIndex, "R810X", "응답 코드가 0이 아님. 응답메시지 : " + refundResponse.getMessage());
                orderService.deleteByIndex(orderEntity.getId());
                return ExternalRefundResponse.builder().responseCode("R8102").message("[R8102] 시스템 에러입니다.").build();
            }

            RefundEntity refundEntity = refundService.save(
                refundResponse.getResponseCode(),
                refundResponse.getPurchaseSequenceNumber(),
                refundResponse.getTakeoutNumber(),
                orderEntity);

            Integer payment = paymentCalculator.paymentInteger(refundEntity);
            pointScheduledChangeService.change(refundEntity, SignType.POSITIVE, franchiseeEntity.getBalancePercentage());
            externalRefundEntity.refundIndexRegister(refundEntity);
            externalRefundEntity.changeStatus(ExternalRefundStatus.APPROVE);

            commonLogger.point2(externalRefundIndex, "성공적으로 승인되었습니다.");

            if (!franchiseeEntity.getIsRefundOnce()) {
                nonBatchPushService.nonBatchPushNSave(PushCategoryType.CASE_FIVE, franchiseeEntity.getId());
                franchiseeEntity.isRefundOnce();
            }
            ExternalRefundResponse externalRefundResponse = ExternalRefundResponse.builder()
                .responseCode(refundResponse.getResponseCode())
                .message(refundResponse.getMessage())
                .payment(payment)
                .build();
            commonLogger.tailLine(externalRefundIndex, "외부 승인 종료");
            return externalRefundResponse;
        } catch (InvalidExternalRefundIndexException e) {
            commonLogger.error1(externalRefundApprovalRequest.getExternalRefundIndex(), "K8103", "인덱스 조회 실패");
            return ExternalRefundResponse.builder().responseCode("K8103").message("[K8103] 시스템 에러입니다.").build();
        } catch (InvalidParameterException e) {
            commonLogger.error1(externalRefundApprovalRequest.getExternalRefundIndex(), "K8104", "어딘가에서 잘못된 파라미터");
            return ExternalRefundResponse.builder().responseCode("K8104").message("[K8104] 시스템 에러입니다.").build();
        } catch (IllegalArgumentException e) {
            commonLogger.error1(externalRefundApprovalRequest.getExternalRefundIndex(), "K8105", "어딘가에서 적절하지 않은 파라미터");
            return ExternalRefundResponse.builder().responseCode("K8105").message("[K8105] 시스템 에러입니다.").build();
        } catch (WebfluxGeneralException e) {
            String code = e.getMessage().substring(0, 4);
            if (code.equals("2001")) {
                commonLogger.error1(externalRefundApprovalRequest.getExternalRefundIndex(), "R8103", "통합한도 초과");
                return ExternalRefundResponse.builder().responseCode("R8103").message("[R8103] 즉시환급 통합한도(250만원) 초과자 입니다.").build();
            } else {
                commonLogger.error1(externalRefundApprovalRequest.getExternalRefundIndex(), "K8109", "관세청 응답코드 0 아님");
                return ExternalRefundResponse.builder().responseCode("K8109").message("[K8109] 시스템 에러입니다.").build();
            }
        } catch (Exception e) {
            commonLogger.error1(externalRefundApprovalRequest.getExternalRefundIndex(), "K8106", "글로벌 에러. 응답메시지 : " + e.getMessage());
            return ExternalRefundResponse.builder().responseCode("K8106").message("[K8106] 시스템 에러입니다.").build();
        }

    }

}
