package com.tpay.domains.external.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.custom.CustomValue;
import com.tpay.commons.exception.detail.InvalidExternalRefundIndexException;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.logger.CommonLogger;
import com.tpay.commons.webClient.WebRequestUtil;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.external.application.dto.ExternalRefundCancelRequest;
import com.tpay.domains.external.application.dto.ExternalRefundResponse;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.point.domain.SignType;
import com.tpay.domains.point_scheduled.application.PointScheduledChangeService;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundCancelRequest;
import com.tpay.domains.refund_core.application.dto.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ExternalRefundCancelService {

    private final ExternalRefundFindService externalRefundFindService;
    private final CustomerFindService customerFindService;
    private final PointScheduledChangeService pointScheduledChangeService;
    private final FranchiseeFindService franchiseeFindService;

    private final WebRequestUtil webRequestUtil;
    private final ObjectMapper objectMapper;
    private final S3FileUploader s3FileUploader;
    private final CommonLogger commonLogger;

    @Transactional
    public ExternalRefundResponse cancel(ExternalRefundCancelRequest externalRefundCancelRequest) {

        try {
            Long externalRefundIndex = externalRefundCancelRequest.getExternalRefundIndex();
            commonLogger.headline(externalRefundIndex, "외부 환급 취소");
            s3FileUploader.deleteBarcode(externalRefundIndex);
            ExternalRefundEntity externalRefundEntity = externalRefundFindService.findById(externalRefundIndex);
            commonLogger.point1(externalRefundIndex, "인덱스 조회 성공");
            ExternalRefundStatus externalRefundStatus = externalRefundEntity.getExternalRefundStatus();
            if (!(externalRefundStatus.equals(ExternalRefundStatus.APPROVE) || externalRefundStatus.equals(ExternalRefundStatus.CONFIRMED))) {
                commonLogger.error1(externalRefundIndex, "K9100", "이미 취소된 건입니다.");
                return ExternalRefundResponse.builder().responseCode("K9100").payment(0).message("[K9100] 이미 취소된 건입니다.").build();
            }

            CustomerEntity customerEntity = customerFindService.findByIndex(externalRefundEntity.getCustomerIndex());
            RefundEntity refundEntity = externalRefundEntity.getRefundEntity();
            RefundCancelRequest refundCancelRequest = RefundCancelRequest.of(customerEntity, refundEntity);

            commonLogger.beforeHttpClient(externalRefundIndex);
            String uri = CustomValue.REFUND_SERVER + "/refund/cancel";
            Object post = webRequestUtil.post(uri, refundCancelRequest);
            RefundResponse refundResponse = objectMapper.convertValue(post, RefundResponse.class);
            commonLogger.afterHttpClient(externalRefundIndex);

            //0000이 아닌경우 에러 발생
            if (!refundResponse.getResponseCode().equals("0000")) {
                commonLogger.error1(externalRefundIndex, "R9105", "응답 코드가 0이 아닙니다. 응답메시지 : " + refundResponse.getMessage());
                return ExternalRefundResponse.builder().responseCode("R9105").message("[R9105] 시스템 에러입니다.").build();
            }

            refundEntity.updateCancel(refundResponse.getResponseCode());
            externalRefundEntity.changeStatus(ExternalRefundStatus.CANCEL);
            FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(externalRefundEntity.getFranchiseeIndex());
            pointScheduledChangeService.change(refundEntity, SignType.NEGATIVE, franchiseeEntity.getBalancePercentage());

            commonLogger.point2(externalRefundIndex, "성공적으로 취소되었습니다.");
            ExternalRefundResponse result = ExternalRefundResponse.builder()
                .responseCode(refundResponse.getResponseCode())
                .payment(0)
                .message(refundResponse.getMessage())
                .build();

            commonLogger.tailLine(externalRefundIndex, "외부 취소 종료");
            return result;
        } catch (InvalidExternalRefundIndexException e) {
            commonLogger.error1(externalRefundCancelRequest.getExternalRefundIndex(), "K9101", "인덱스 조회 실패");
            return ExternalRefundResponse.builder().responseCode("K9101").payment(0).message("[K9101] 시스템 에러입니다.").build();
        } catch (InvalidParameterException e) {
            commonLogger.error1(externalRefundCancelRequest.getExternalRefundIndex(), "K9102", "어딘가에서 잘못된 파라미터");
            return ExternalRefundResponse.builder().responseCode("K9102").payment(0).message("[K9102] 시스템 에러입니다.").build();
        } catch (IllegalArgumentException e) {
            commonLogger.error1(externalRefundCancelRequest.getExternalRefundIndex(), "K9103", "어딘가에서 적절하지 않은 파라미터");
            return ExternalRefundResponse.builder().responseCode("K9103").payment(0).message("[K9103] 시스템 에러입니다.").build();
        } catch (Exception e) {
            commonLogger.error1(externalRefundCancelRequest.getExternalRefundIndex(), "K9104", "글로벌 에러. 응답메시지 : " + e.getMessage());
            return ExternalRefundResponse.builder().responseCode("K9104").payment(0).message("[K9104] 시스템 에러입니다.").build();
        }
    }
}
