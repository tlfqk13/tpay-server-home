package com.tpay.domains.refund.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.refund.application.dto.RefundFindResponse;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefundFindService {

  private final RefundRepository refundRepository;

  public RefundEntity findByIndex(Long refundIndex) {
    RefundEntity refundEntity =
        refundRepository
            .findById(refundIndex)
            .orElseThrow(
                () ->
                    new InvalidParameterException(
                        ExceptionState.INVALID_PARAMETER, "Invalid Refund Index"));

    return refundEntity;
  }

  public List<RefundFindResponse> findList(Long franchiseeIndex) {
    List<RefundEntity> refundEntityList =
        refundRepository.findAllByFranchiseeIndex(franchiseeIndex);

    return refundEntityList.stream()
        .map(
            refundEntity ->
                RefundFindResponse.builder()
                    .refundIndex(refundEntity.getId())
                    .createdDate(refundEntity.getCreatedDate())
                    .orderNumber(refundEntity.getOrderEntity().getOrderNumber())
                    .totalAmount(refundEntity.getOrderEntity().getTotalAmount())
                    .totalRefund(refundEntity.getTotalRefund())
                    .refundStatus(refundEntity.getRefundStatus())
                    .build())
        .collect(Collectors.toList());
  }
}
