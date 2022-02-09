package com.tpay.domains.refund.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.DateFilter;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.application.dto.CustomerInfo;
import com.tpay.domains.refund.application.dto.*;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RefundFindService {

  private final PassportNumberEncryptService passportNumberEncryptService;
  private final RefundRepository refundRepository;
  private final CustomerFindService customerFindService;

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

  public List<RefundFindResponseInterface> findAll() {
    List<RefundFindResponseInterface> result = refundRepository.findAllNativeQuery();
    return result;
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

  public List<RefundFindResponseInterface> findAFranchisee(Long franchiseeIndex) {
    return refundRepository.findAFranchiseeNativeQuery(franchiseeIndex);
  }

  public List<RefundByCustomerResponse> findRefundsByCustomerInfo(Long franchiseeIndex, RefundCustomerRequest refundCustomerRequest) {
    RefundCustomerInfoRequest refundCustomerInfoRequest = refundCustomerRequest.getRefundCustomerInfoRequest();
    RefundCustomerDateRequest refundCustomerDateRequest = refundCustomerRequest.getRefundCustomerDateRequest();

    String startDate = refundCustomerDateRequest.getStartDate();
    String endDate = refundCustomerDateRequest.getEndDate();
    String orderCheck = refundCustomerDateRequest.getOrderCheck();

    String name = refundCustomerInfoRequest.getName();
    String nation = refundCustomerInfoRequest.getNationality();
    String passportNumber = refundCustomerInfoRequest.getPassportNumber();
    Long customerIndex = customerFindService.findByNationAndPassportNumber(name, passportNumber, nation).getId();

    List<RefundFindResponseInterface> refundsByCustomerInfo = refundRepository.findRefundsByCustomerInfo(franchiseeIndex, startDate, endDate, customerIndex);
    List<RefundByCustomerResponse> refundByCustomerResponseList;
    if(orderCheck.equals("ASC")) {
      refundByCustomerResponseList = refundsByCustomerInfo.stream()
          .map(refundFindResponseInterface ->
              RefundByCustomerResponse.builder()
                  .refundIndex(refundFindResponseInterface.getRefundIndex())
                  .orderNumber(refundFindResponseInterface.getOrderNumber())
                  .createdDate(refundFindResponseInterface.getCreatedDate())
                  .totalAmount(refundFindResponseInterface.getTotalAmount())
                  .totalRefund(refundFindResponseInterface.getTotalRefund())
                  .refundStatus(refundFindResponseInterface.getRefundStatus())
                  .build())
          .sorted(Comparator.comparing(RefundByCustomerResponse::getCreatedDate))
          .collect(Collectors.toList());
    }
    else {
      refundByCustomerResponseList = refundsByCustomerInfo.stream()
          .map(refundFindResponseInterface ->
              RefundByCustomerResponse.builder()
                  .refundIndex(refundFindResponseInterface.getRefundIndex())
                  .orderNumber(refundFindResponseInterface.getOrderNumber())
                  .createdDate(refundFindResponseInterface.getCreatedDate())
                  .totalAmount(refundFindResponseInterface.getTotalAmount())
                  .totalRefund(refundFindResponseInterface.getTotalRefund())
                  .refundStatus(refundFindResponseInterface.getRefundStatus())
                  .build())
          .sorted(Comparator.comparing(RefundByCustomerResponse::getCreatedDate).reversed())
          .collect(Collectors.toList());
    }
    return refundByCustomerResponseList;

  }
}
