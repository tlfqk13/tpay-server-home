package com.tpay.domains.external.application;

import com.tpay.domains.external.application.dto.ExternalResultStatus;
import com.tpay.domains.external.application.dto.ExternalStatusRequestResponse;
import com.tpay.domains.external.application.dto.ExternalStatusUpdateDto;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.external.domain.ExternalRepository;
import com.tpay.domains.order.domain.OrderEntity;
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
            OrderEntity orderEntity = refundEntity.getOrderEntity();
            String totalAmount = orderEntity.getTotalAmount();
            int totalAmountInt = Integer.parseInt(orderEntity.getTotalAmount());
            int totalRefundInt = Integer.parseInt(refundEntity.getTotalRefund());
            RefundData refundData = new RefundData(String.valueOf(totalAmountInt - totalRefundInt), totalAmount, refundEntity.getTotalRefund());
            ExternalStatusRequestResponse externalStatusRequestResponse = new ExternalStatusRequestResponse(ExternalResultStatus.SUCCESS, refundData, "");
            return ResponseEntity.status(HttpStatus.OK).body(externalStatusRequestResponse);
        } else {
            // TODO: 2022/04/29 Exception
            ExternalStatusRequestResponse externalStatusRequestResponse = new ExternalStatusRequestResponse(ExternalResultStatus.IN_PROGRESS, new RefundData(), "Payment is in progress.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(externalStatusRequestResponse);
        }

    }

    @Transactional
    public void statusUpdate(Long externalRefundIndex, ExternalStatusUpdateDto externalStatusUpdateDto) {
        ExternalRefundEntity externalRefundEntity = externalRefundFindService.findById(externalRefundIndex);
        externalRefundEntity.changeStatus(externalStatusUpdateDto.getExternalRefundStatus());
    }
}
