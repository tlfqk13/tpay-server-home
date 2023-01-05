package com.tpay.domains.refund.application;

import com.tpay.domains.customer.application.CustomerService;
import com.tpay.domains.customer.application.dto.DepartureStatus;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.erp.test.dto.RefundType;
import com.tpay.domains.order.application.dto.CmsDto;
import com.tpay.domains.refund.application.dto.*;
import com.tpay.domains.refund.domain.PaymentStatus;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefundDetailFindService {

    private final RefundRepository refundRepository;
    private final CustomerService customerService;

    public List<RefundFindResponseInterface> findList(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {

        LocalDate newEnd = endDate.plusDays(1);
        return refundRepository.findAllByFranchiseeIndex(franchiseeIndex, startDate.atTime(0, 0), newEnd.atTime(0, 0));
    }

    public RefundPagingFindResponse findAll(int page, RefundStatus refundStatus, String startDate, String endDate, String searchKeyword, boolean isRefundAfter) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startLocalDate = LocalDate.parse("20" + startDate, yyyyMMdd);
        LocalDate endLocalDate = LocalDate.parse("20" + endDate, yyyyMMdd).plusDays(1);
        PageRequest pageRequest = PageRequest.of(page, 10);
        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);

        Page<RefundFindAllDto.Response> response = refundRepository.findRefundAll(
                pageRequest, startLocalDate, endLocalDate, searchKeyword.isEmpty()
                ,isBusinessNumber, searchKeyword, refundStatus, RefundType.ALL
                , DepartureStatus.ALL, PaymentStatus.ALL);

        int totalPage = response.getTotalPages();
        if(totalPage != 0){
            totalPage = totalPage -1;
        }

        return RefundPagingFindResponse.builder()
                .totalPage(totalPage)
                .refundFindResponseInterfaceList(response.getContent())
                .build();
    }

    public RefundDetailTotalDto.Response findRefundDetail(Long franchiseeIndex, String startDate, String endDate) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startLocalDate = LocalDate.parse("20" + startDate, yyyyMMdd);
        LocalDate endLocalDate = LocalDate.parse("20" + endDate, yyyyMMdd).plusDays(1);

        List<RefundFindDto.Response> findAFranchiseeList = refundRepository.findRefundDetail(franchiseeIndex,startLocalDate,endLocalDate);
        RefundFindResponseInterface refundDetailList = refundRepository.findRefundDetailSaleTotalQuery(franchiseeIndex, startLocalDate, endLocalDate);

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

    public List<RefundByCustomerDateResponse> findRefundsByCustomerInfo(Long franchiseeIndex, RefundCustomerRequest refundCustomerRequest) {
        RefundCustomerInfoRequest refundCustomerInfoRequest = refundCustomerRequest.getRefundCustomerInfoRequest();
        RefundCustomerDateRequest refundCustomerDateRequest = refundCustomerRequest.getRefundCustomerDateRequest();

        LocalDate startDate = refundCustomerDateRequest.getStartDate();
        LocalDate endDate = refundCustomerDateRequest.getEndDate().plusDays(1);
        String orderCheck = refundCustomerDateRequest.getOrderCheck();

        String nation = refundCustomerInfoRequest.getNationality();
        String passportNumber = refundCustomerInfoRequest.getPassportNumber();
        Optional<CustomerEntity> customerEntityOptional = customerService.findCustomerByNationAndPassportNumber(passportNumber, nation);

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

    private boolean includeZeroInPassportNumber(String passportNumber) {
        String secondCharInPassport = passportNumber.substring(1, 2);
        return secondCharInPassport.equals("0") ||
                secondCharInPassport.equals("O");
    }

    private List<String> getAvailPassportNumberList(String passportNumber) {
        List<String> passportNumList = new ArrayList<>();
        passportNumList.add(passportNumber);

        StringBuilder stringBuilder = new StringBuilder(passportNumber);
        char secondChar = stringBuilder.charAt(1);
        if (secondChar == '0') {
            stringBuilder.setCharAt(1, 'O');
        } else {
            stringBuilder.setCharAt(1, '0');
        }

        passportNumList.add(stringBuilder.toString());
        return passportNumList;
    }

}
