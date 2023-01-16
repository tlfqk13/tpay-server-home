package com.tpay.domains.erp.test.service;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.application.dto.CustomerPaymentType;
import com.tpay.domains.customer.application.dto.DepartureStatus;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.refund.application.dto.*;
import com.tpay.domains.refund.domain.PaymentStatus;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ErpRefundDetailFindTestService {

    private final RefundRepository refundRepository;
    private final CustomerService customerService;

    public RefundPaymentDto.Response findPaymentDetail(Long refundIndex) {

        RefundDetailDto.Response detailRefundInfo = refundRepository.findRefundDetail(refundIndex);
        RefundPaymentDetailDto.Response detailPaymentDto = refundRepository.findRefundPaymentDetail(refundIndex);

        String paymentInfo;
        if (CustomerPaymentType.CASH.equals(detailPaymentDto.getCustomerPaymentType())) {
            paymentInfo = detailPaymentDto.getCustomerAccountNumber() + '/' + detailPaymentDto.getCustomerBankName();
        } else {
            paymentInfo = detailPaymentDto.getCustomerCreditNumber();
        }

        RefundPaymentInfoDto.Response detailPaymentInfo =
                RefundPaymentInfoDto.Response.builder()
                        .customerPaymentType(detailPaymentDto.getCustomerPaymentType())
                        .paymentInfo(paymentInfo)
                        .build();


        RefundPaymentDto.Response response =
                RefundPaymentDto.Response.builder()
                        .detailRefundInfo(detailRefundInfo)
                        .detailPaymentInfo(detailPaymentInfo)
                        .paymentStatus(detailRefundInfo.getPaymentStatus())
                        .build();

        return response;
    }

    @Transactional
    public void updatePaymentDetail(Long refundIndex, RefundPaymentDto.Request request) {

        RefundEntity refundEntity = refundRepository.findById(refundIndex)
                .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid RefundIndex"));
        refundEntity.getRefundAfterEntity().updatePaymentStatus(request.getPaymentStatus());

        CustomerEntity customerEntity = customerService.findByIndex(refundEntity.getOrderEntity().getCustomerEntity().getId());

        CustomerPaymentType customerPaymentType = request.getDetailPaymentInfo().getCustomerPaymentType();

        String paymentInfo = request.getDetailPaymentInfo().getPaymentInfo();

        if(CustomerPaymentType.CASH.equals(customerPaymentType)){
            String customerBankName = paymentInfo.substring(paymentInfo.indexOf('/') + 1);
            String customerAccountNumber = paymentInfo.substring(0,paymentInfo.indexOf('/')-1);
            customerEntity.updateCustomerPaymentInfo(customerPaymentType, null,
                    customerBankName,customerAccountNumber);
        }else{
            customerEntity.updateCustomerPaymentInfo(customerPaymentType, paymentInfo,
                    null,null);
        }
    }

    @Transactional
    public void registerPaymentDetail(Long refundIndex, RefundPaymentDto.Request request) {

        updatePaymentDetail(refundIndex, request);

    }

    public RefundDetailTotalDto.Response findRefundDetail(Long franchiseeIndex, String startDate, String endDate) {

        List<RefundFindDto.Response> findAFranchiseeList = refundRepository.findRefundDetail(franchiseeIndex, getStartDate(startDate, DateTimeFormatter.ofPattern("yyyyMMdd")), getEndDate(endDate, DateTimeFormatter.ofPattern("yyyyMMdd")));
        RefundFindResponseInterface refundDetailList = refundRepository.findRefundDetailSaleTotalQuery(franchiseeIndex, getStartDate(startDate, DateTimeFormatter.ofPattern("yyyyMMdd")), getEndDate(endDate, DateTimeFormatter.ofPattern("yyyyMMdd")));

        RefundDetailTotalResponse refundDetail = RefundDetailTotalResponse.builder()
                .totalActualAmount(refundDetailList.getActualAmount())
                .totalAmount(refundDetailList.getTotalAmount())
                .totalRefund(refundDetailList.getTotalRefund())
                .totalCancel(refundDetailList.getCancelCount())
                .totalCount(refundDetailList.getSaleCount())
                .build();

        return RefundDetailTotalDto.Response.builder()
                .totalRefundData(refundDetail)
                .refundList(findAFranchiseeList)
                .build();
    }

    public Page<RefundFindAllDto.Response> findAll(Pageable pageable, String startDate, String endDate, String searchKeyword
            , RefundType refundType, RefundStatus refundStatus, DepartureStatus departureStatus, PaymentStatus paymentStatus) {

        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);

        return refundRepository.findRefundAll(
                pageable, getStartDate(startDate, DateTimeFormatter.ofPattern("yyyyMMdd")), getEndDate(endDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
                , searchKeyword.isEmpty(), isBusinessNumber, searchKeyword, refundStatus
                , refundType, departureStatus, paymentStatus);
    }

    private LocalDate getEndDate(String endDate, DateTimeFormatter yyyyMMdd) {
        return LocalDate.parse("20" + endDate, yyyyMMdd).plusDays(1);
    }

    private LocalDate getStartDate(String startDate, DateTimeFormatter yyyyMMdd) {
        return LocalDate.parse("20" + startDate, yyyyMMdd);
    }
}
