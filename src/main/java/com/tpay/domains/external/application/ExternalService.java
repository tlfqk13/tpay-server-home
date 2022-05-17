package com.tpay.domains.external.application;

import com.tpay.domains.external.application.dto.ExternalRefundResponse;
import com.tpay.domains.external.application.dto.ExternalResultStatus;
import com.tpay.domains.external.application.dto.ExternalStatusRequestResponse;
import com.tpay.domains.external.application.dto.ExternalStatusUpdateDto;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.external.domain.ExternalRepository;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund_core.application.dto.RefundData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalService {

    private final ExternalRepository externalRepository;
    private final ExternalRefundFindService externalRefundFindService;
    private final PaymentCalculator paymentCalculator;


    @Transactional
    public ExternalRefundEntity save(Long franchiseeIndex, Long customerIndex, String deduction) {
        ExternalRefundEntity externalRefundEntity = new ExternalRefundEntity(franchiseeIndex, customerIndex, ExternalRefundStatus.SCAN, deduction);
        return externalRepository.save(externalRefundEntity);
    }

    public ResponseEntity<ExternalStatusRequestResponse> statusRequest(Long externalRefundIndex) {
        ExternalRefundEntity externalRefundEntity = externalRefundFindService.findById(externalRefundIndex);
        ExternalRefundStatus externalRefundStatus = externalRefundEntity.getExternalRefundStatus();
        if (externalRefundStatus.equals(ExternalRefundStatus.CANCEL)) {
            ExternalStatusRequestResponse externalStatusRequestResponse = new ExternalStatusRequestResponse(ExternalResultStatus.FAIL, new RefundData(), "");
            return ResponseEntity.status(HttpStatus.OK).body(externalStatusRequestResponse);
        } else if (externalRefundStatus.equals(ExternalRefundStatus.CONFIRMED)) {
            RefundEntity refundEntity = externalRefundEntity.getRefundEntity();
            String paymentString = paymentCalculator.paymentString(refundEntity);
            RefundData refundData = new RefundData(paymentString, refundEntity.getOrderEntity().getTotalAmount(), refundEntity.getTotalRefund());
            ExternalStatusRequestResponse externalStatusRequestResponse = new ExternalStatusRequestResponse(ExternalResultStatus.SUCCESS, refundData, "");
            return ResponseEntity.status(HttpStatus.OK).body(externalStatusRequestResponse);
        } else {
            // TODO: 2022/04/29 Exception
            ExternalStatusRequestResponse externalStatusRequestResponse = new ExternalStatusRequestResponse(ExternalResultStatus.IN_PROGRESS, new RefundData(), "Payment is in progress.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(externalStatusRequestResponse);
        }

    }

    @Transactional
    public ExternalRefundResponse statusUpdate(Long externalRefundIndex, ExternalStatusUpdateDto externalStatusUpdateDto) {
        ExternalRefundEntity externalRefundEntity = externalRefundFindService.findById(externalRefundIndex);
        RefundEntity refundEntity = externalRefundEntity.getRefundEntity();
        String paymentFromEntity = paymentCalculator.paymentString(refundEntity);
        String paymentFromExternal = externalStatusUpdateDto.getPayment();
        if (!paymentFromEntity.equals(paymentFromExternal)) {
            log.error("CODE[8105] - externalRefundIndex : {} paymentFromEntity : {} paymentFromExternal : {}", externalRefundEntity.getId(), paymentFromEntity, paymentFromExternal);
            return ExternalRefundResponse.builder().responseCode("8105").payment(0).message("시스템 에러입니다.").build();
        } else {
            externalRefundEntity.changeStatus(externalStatusUpdateDto.getExternalRefundStatus());
            return ExternalRefundResponse.builder().responseCode("0000").payment(0).message("").build();
        }

    }
}
