package com.tpay.domains.refund.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.DateFilter;
import com.tpay.domains.customer.application.dto.CustomerInfo;
import com.tpay.domains.refund.application.dto.RefundFindResponse;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefundFindService {

  private final PassportNumberEncryptService passportNumberEncryptService;
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

  public List<RefundFindResponse> findList(
      Long franchiseeIndex, DateFilter dateFilter, LocalDate startDate, LocalDate endDate) {

    if (endDate != null) {
      endDate = endDate.plusDays(1);
    }

    if (dateFilter != DateFilter.CUSTOM) {
      startDate = dateFilter.getStartDate();
      endDate = dateFilter.getEndDate();
    }

    List<RefundEntity> refundEntityList =
        refundRepository.findAllByFranchiseeIndex(
            franchiseeIndex, startDate.atTime(0, 0), endDate.atTime(0, 0));

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

  public List<RefundFindResponse> findAll() {
    List<RefundEntity> refundEntities = refundRepository.findAll();
    return refundEntities.stream()
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

  public List<RefundFindResponse> findAllByCustomerInfo(
      Long franchiseeIndex, CustomerInfo customerInfo, DateFilter dateFilter, LocalDate startDate, LocalDate endDate) {
    if (endDate != null) {
      endDate = endDate.plusDays(1);
    }

    if (dateFilter != DateFilter.CUSTOM) {
      startDate = dateFilter.getStartDate();
      endDate = dateFilter.getEndDate();
    }

    String passportNumber = passportNumberEncryptService.encrypt(customerInfo.getPassportNumber());
    List<RefundEntity> refundEntityList =
        refundRepository.findAllByPassportNumber(
            franchiseeIndex, passportNumber, startDate.atTime(0, 0), endDate.atTime(0, 0));

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
