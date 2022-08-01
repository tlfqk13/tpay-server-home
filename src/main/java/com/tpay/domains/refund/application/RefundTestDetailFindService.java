package com.tpay.domains.refund.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.util.DateFilter;
import com.tpay.domains.customer.application.CustomerFindService;
import com.tpay.domains.customer.application.dto.CustomerInfo;
import com.tpay.domains.refund.application.dto.*;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.search.application.dto.SearchRefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefundTestDetailFindService {

    private final PassportNumberEncryptService passportNumberEncryptService;
    private final RefundRepository refundRepository;
    private final CustomerFindService customerFindService;
    private final SearchRefundRepository searchRefundRepository;

    public List<RefundFindResponseInterface> findList(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {

        LocalDate newEnd = endDate.plusDays(1);
        return refundRepository.findAllByFranchiseeIndex(franchiseeIndex, startDate.atTime(0, 0), newEnd.atTime(0, 0));


    }

    public RefundPagingFindResponse findAll(int page, String startDate, String endDate, RefundStatus refundStatus, String searchKeyword) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startLocalDate = LocalDate.parse("20" + startDate, yyyyMMdd);
        LocalDate endLocalDate = LocalDate.parse("20" + endDate, yyyyMMdd).plusDays(1);
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<RefundFindResponseInterface> refundFindResponseInterfaces = null;
        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);

        if(refundStatus.equals(RefundStatus.ALL)) {
            if (searchKeyword.isEmpty()) {
                refundFindResponseInterfaces = refundRepository.findAllNativeQueryTest(pageRequest, startLocalDate, endLocalDate);
            } else {
                if (isBusinessNumber) {
                    //사업자 번호로 검색
                    refundFindResponseInterfaces = searchRefundRepository.SearchFindByBusinessNumberTest(pageRequest, startLocalDate, endLocalDate, searchKeyword);
                } else {
                    //가게 이름으로 검색
                    refundFindResponseInterfaces = searchRefundRepository.SearchFindByStoreNameTest(pageRequest, startLocalDate, endLocalDate, searchKeyword);
                }
            }
        }else{
            int ordinal = refundStatus.ordinal();
            if (searchKeyword.isEmpty()) {
                refundFindResponseInterfaces = refundRepository.findRefundStatusNativeQueryTest(pageRequest, startLocalDate, endLocalDate,ordinal);
            } else {
                if (isBusinessNumber) {
                    //사업자 번호로 검색
                    refundFindResponseInterfaces = searchRefundRepository.SearchFindByBusinessNumberTest(pageRequest, startLocalDate, endLocalDate, searchKeyword,ordinal);
                } else {
                    //가게 이름으로 검색
                    refundFindResponseInterfaces = searchRefundRepository.SearchFindByStoreNameTest(pageRequest, startLocalDate, endLocalDate, searchKeyword,ordinal);
                }
            }
        }

        List<RefundFindResponseInterface> list = refundFindResponseInterfaces.getContent();
        return RefundPagingFindResponse.builder()
                .totalPage(refundFindResponseInterfaces.getTotalPages()-1)
                .refundFindResponseInterfaceList(list)
                .build();
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

    public List<RefundByCustomerDateResponse> findRefundsByCustomerInfo(Long franchiseeIndex, RefundCustomerRequest refundCustomerRequest) {
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

    public List<RefundFindResponseInterface> findDetail(Long franchiseeIndex,Long refundIndex) {
        return refundRepository.findDetailNativeQuery(franchiseeIndex,refundIndex);
    }

    public List<List<String>> findFranchiseeId(String year, String month) {
        List<RefundFindResponseInterface> refundFindResponseInterfaceList = refundRepository.findFranchiseeId(year, month);
        List<List<String>> result = new ArrayList<>();
        for(RefundFindResponseInterface refundFindResponseInterface : refundFindResponseInterfaceList ){
            List<String> baseList = new ArrayList<>();
            baseList.add(refundFindResponseInterface.getFranchiseeId());
            result.add(baseList);
        }
        return result;
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


}
