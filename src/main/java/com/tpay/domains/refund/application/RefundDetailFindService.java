package com.tpay.domains.refund.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.util.IndexInfo;
import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.application.dto.CustomerPaymentType;
import com.tpay.domains.customer.application.dto.DepartureStatus;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.employee.application.EmployeeFindService;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.order.application.dto.CmsDto;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.tpay.commons.util.UserSelector.EMPLOYEE;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefundDetailFindService {

    private final RefundRepository refundRepository;
    private final CustomerService customerService;
    private final EmployeeFindService employeeFindService;

    public List<RefundFindResponseInterface> findList(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {

        LocalDate newEnd = endDate.plusDays(1);
        return refundRepository.findAllByFranchiseeIndex(franchiseeIndex, startDate.atTime(0, 0), newEnd.atTime(0, 0));
    }

    public Page<RefundFindAllDto.Response>  findAll(Pageable pageable, String startDate, String endDate, String searchKeyword
            , RefundType refundType, RefundStatus refundStatus, DepartureStatus departureStatus, PaymentStatus paymentStatus) {


        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);

        return refundRepository.findRefundAll(
                pageable, getStartDate(startDate, DateTimeFormatter.ofPattern("yyyyMMdd")), getEndDate(endDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
                , searchKeyword.isEmpty(), isBusinessNumber, searchKeyword, refundStatus
                , refundType, departureStatus, paymentStatus);
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

    public List<RefundByCustomerDateResponse> findRefundsByCustomerInfo(IndexInfo indexInfo, RefundCustomerRequest refundCustomerRequest) {
        RefundCustomerInfoRequest refundCustomerInfoRequest = refundCustomerRequest.getRefundCustomerInfoRequest();
        RefundCustomerDateRequest refundCustomerDateRequest = refundCustomerRequest.getRefundCustomerDateRequest();

        LocalDate startDate = refundCustomerDateRequest.getStartDate();
        LocalDate endDate = refundCustomerDateRequest.getEndDate().plusDays(1);
        String orderCheck = refundCustomerDateRequest.getOrderCheck();

        String nation = refundCustomerInfoRequest.getNationality();
        String passportNumber = refundCustomerInfoRequest.getPassportNumber();
        Optional<CustomerEntity> customerEntityOptional = customerService.findCustomerByNationAndPassportNumber(passportNumber, nation);

        Long franchiseeIndex = getFranchiseeIndex(indexInfo);

        if(customerEntityOptional.isEmpty()) {
            return Collections.emptyList();
        }
        Long customerIndex = customerEntityOptional.get().getId();
        // TODO: 2022/11/01 환급 취소 리스트 조회 -> dsl 로 바꿔야함
        List<RefundFindResponseInterface> refundsByCustomerInfo = refundRepository.findRefundsByCustomerInfo(franchiseeIndex, startDate, endDate, customerIndex);



        if (refundsByCustomerInfo.isEmpty()) {
            return Collections.emptyList();
        }
        List<RefundByCustomerResponse> refundByCustomerResponseList =
            refundsByCustomerInfo.stream()
                .map(RefundByCustomerResponse::from)
                .collect(Collectors.toList());
        Queue<RefundByCustomerResponse> refundByCustomerResponseQueue = new PriorityQueue<>();
        if (orderCheck.equals("ASC")) {
            refundByCustomerResponseQueue = new PriorityQueue<>(new ResponseCompAsc());
        }else if (orderCheck.equals("DESC")) {
            refundByCustomerResponseQueue = new PriorityQueue<>(new ResponseCompDesc());
        }
        refundByCustomerResponseQueue.addAll(refundByCustomerResponseList);

        List<RefundByCustomerDateResponse> refundByCustomerDateResponseList = new ArrayList<>();
        List<RefundByCustomerDateResponse.Data> dataList = new ArrayList<>();
        LocalDate targetDate = refundByCustomerResponseQueue.element().getFormatDate();
        while (!refundByCustomerResponseQueue.isEmpty()) {
            RefundByCustomerResponse poll = refundByCustomerResponseQueue.poll();
            if (poll.getFormatDate().equals(targetDate)) {
                dataList.add(new RefundByCustomerDateResponse.Data(poll));
            } else {
                refundByCustomerDateResponseList.add(RefundByCustomerDateResponse.builder().date(targetDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))).dataList(dataList).build());
                dataList = new ArrayList<>();
                dataList.add(new RefundByCustomerDateResponse.Data(poll));
                targetDate = poll.getFormatDate();
            }
            if (refundByCustomerResponseQueue.isEmpty()) {
                refundByCustomerDateResponseList.add(RefundByCustomerDateResponse.builder().date(targetDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))).dataList(dataList).build());
            }
        }

        return refundByCustomerDateResponseList;
    }

    public List<List<String>> findFranchiseeId(LocalDate startDate, LocalDate endDate) {
        List<CmsDto.Response> refundFindResponseInterfaceList = refundRepository.findFranchiseeIdCmsService(startDate,endDate);
        List<List<String>> result = new ArrayList<>();
        for(CmsDto.Response refundFindResponseInterface : refundFindResponseInterfaceList ){
            List<String> baseList = new ArrayList<>();
            baseList.add(String.valueOf(refundFindResponseInterface.getFranchiseeIndex()));
            result.add(baseList);
        }
        return result;
    }

    // 사후
    public List<List<String>> findFranchiseeIdAfter(LocalDate startDate, LocalDate endDate) {
        List<CmsDto.Response> refundFindResponseInterfaceList = refundRepository.findFranchiseeIdAfter(startDate,endDate);
        List<List<String>> result = new ArrayList<>();
        for(CmsDto.Response refundFindResponseInterface : refundFindResponseInterfaceList ){
            List<String> baseList = new ArrayList<>();
            baseList.add(String.valueOf(refundFindResponseInterface.getFranchiseeIndex()));
            result.add(baseList);
        }
        return result;
    }

    public RefundPaymentDto.Response findPaymentDetail(Long refundIndex) {

        RefundDetailDto.Response detailRefundInfo = refundRepository.findRefundDetail(refundIndex);
        RefundPaymentDetailDto.Response detailPaymentDto = refundRepository.findRefundPaymentDetail(refundIndex);

        String paymentInfo;
        if (CustomerPaymentType.CASH.equals(detailPaymentDto.getCustomerPaymentType())) {
            paymentInfo = detailPaymentDto.getCustomerAccountNumber();
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
            String customerBankName = paymentInfo.substring(paymentInfo.indexOf('|') + 1);
            String customerAccountNumber = paymentInfo.substring(0,paymentInfo.indexOf('|')-1);
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

    private static class ResponseCompAsc implements Comparator<RefundByCustomerResponse> {

        @Override
        public int compare(RefundByCustomerResponse o1, RefundByCustomerResponse o2) {
            return o1.getCreatedDate().compareTo(o2.getCreatedDate());
        }

    }
    private static class ResponseCompDesc implements Comparator<RefundByCustomerResponse> {

        @Override
        public int compare(RefundByCustomerResponse o1, RefundByCustomerResponse o2) {
            return o2.getCreatedDate().compareTo(o1.getCreatedDate());
        }
    }
    private LocalDate getEndDate(String endDate, DateTimeFormatter yyyyMMdd) {
        return LocalDate.parse("20" + endDate, yyyyMMdd).plusDays(1);
    }

    private LocalDate getStartDate(String startDate, DateTimeFormatter yyyyMMdd) {
        return LocalDate.parse("20" + startDate, yyyyMMdd);
    }

    private Long getFranchiseeIndex(IndexInfo indexInfo) {
        if (EMPLOYEE == indexInfo.getUserSelector()) {
            EmployeeEntity employeeEntity = employeeFindService.findById(indexInfo.getIndex())
                    .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Employee not exists"));
            return employeeEntity.getFranchiseeEntity().getId();
        }

        return indexInfo.getIndex();
    }

}
