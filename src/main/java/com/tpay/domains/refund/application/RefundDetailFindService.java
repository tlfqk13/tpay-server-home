package com.tpay.domains.refund.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.DateFilter;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.application.dto.CustomerInfo;
import com.tpay.domains.order.application.OrderFindService;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.refund.application.dto.*;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RefundDetailFindService {

    private final PassportNumberEncryptService passportNumberEncryptService;
    private final RefundRepository refundRepository;
    private final RefundFindService refundFindService;
    private final CustomerFindService customerFindService;
    private final OrderFindService orderFindService;

    public List<RefundFindResponseInterface> findList(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {

        // TODO: 2022/04/28 refundEntityList로 바꾸기 진행 중
        OrderEntity orderEntity = orderFindService.findByFranchiseeId(franchiseeIndex);
        RefundEntity refundEntity = refundFindService.findByOrderEntity(orderEntity);
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = startDate.plusDays(1).atStartOfDay();
        List<RefundEntity> refundEntityList = refundRepository.findAllByIdAndCreatedDateBetween(refundEntity.getId(), startTime, endTime);


        return refundRepository.findAllByFranchiseeIndex(franchiseeIndex, startDate.atTime(0, 0), endDate.atTime(0, 0));


    }

    public List<RefundFindResponseInterface> findAll(String startDate, String endDate, RefundStatus refundStatus) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startLocalDate = LocalDate.parse("20" + startDate, yyyyMMdd);
        LocalDate endLocalDate = LocalDate.parse("20" + endDate, yyyyMMdd);

        if (refundStatus.equals(RefundStatus.ALL)) {
            return refundRepository.findAllNativeQuery(startLocalDate, endLocalDate);
        } else {
            int ordinal = refundStatus.ordinal();
            return refundRepository.findRefundStatusNativeQuery(startLocalDate, endLocalDate, ordinal);
        }

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

        LocalDate startDate = refundCustomerDateRequest.getStartDate();
        LocalDate endDate = refundCustomerDateRequest.getEndDate().plusDays(1);
        String orderCheck = refundCustomerDateRequest.getOrderCheck();

        String name = refundCustomerInfoRequest.getName();
        String nation = refundCustomerInfoRequest.getNationality();
        String passportNumber = refundCustomerInfoRequest.getPassportNumber();
        Long customerIndex = customerFindService.findByNationAndPassportNumber(name, passportNumber, nation).getId();

        List<RefundFindResponseInterface> refundsByCustomerInfo = refundRepository.findRefundsByCustomerInfo(franchiseeIndex, startDate, endDate, customerIndex);
        List<RefundByCustomerResponse> refundByCustomerResponseList =
            refundsByCustomerInfo.stream()
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
        if (orderCheck.equals("ASC")) {
            return refundByCustomerResponseList;
        } else if (orderCheck.equals("DESC")) {
            return refundByCustomerResponseList.stream()
                .sorted(Comparator.comparing(RefundByCustomerResponse::getCreatedDate).reversed())
                .collect(Collectors.toList());
        } else {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "invalid orderCheck");
        }
    }
}
