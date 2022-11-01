package com.tpay.domains.refund_test.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.domains.customer.application.CustomerUpdateService;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.refund.application.dto.*;
import com.tpay.domains.refund.domain.RefundRepository;
import com.tpay.domains.refund.domain.RefundStatus;
import com.tpay.domains.refund_test.dto.RefundDetailTotalDto;
import com.tpay.domains.refund_test.dto.RefundFindAllDto;
import com.tpay.domains.refund_test.dto.RefundFindDto;
import com.tpay.domains.refund_test.dto.RefundTestPagingFindResponse;
import com.tpay.domains.search.application.dto.SearchRefundRepository;
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
public class RefundTestDetailFindService {

    private final PassportNumberEncryptService passportNumberEncryptService;
    private final RefundRepository refundRepository;
    private final CustomerUpdateService customerUpdateService;
    private final SearchRefundRepository searchRefundRepository;

    public List<RefundFindResponseInterface> findList(Long franchiseeIndex, LocalDate startDate, LocalDate endDate) {
        log.trace(" @@  = findList @@ ");
        LocalDate newEnd = endDate.plusDays(1);
        return refundRepository.findAllByFranchiseeIndex(franchiseeIndex, startDate.atTime(0, 0), newEnd.atTime(0, 0));
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
        Optional<CustomerEntity> customerEntityOptional = Optional.empty();
        if (includeZeroInPassportNumber(passportNumber)) {
            List<String> availPassportNumbers = getAvailPassportNumberList(passportNumber);
            for (String passportNum : availPassportNumbers) {
                customerEntityOptional = customerUpdateService.findCustomerByNationAndPassportNumber(passportNum, nation);
                if(customerEntityOptional.isPresent()) {
                    log.debug("Applied passport Number = {}", passportNum);
                    break;
                }
            }
        }

        if(customerEntityOptional.isEmpty()) {
            return Collections.emptyList();
        }
        Long customerIndex = customerEntityOptional.get().getId();
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

    public RefundTestPagingFindResponse findAll(int page, RefundStatus refundStatus, String startDate, String endDate, String searchKeyword) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startLocalDate = LocalDate.parse("20" + startDate, yyyyMMdd);
        LocalDate endLocalDate = LocalDate.parse("20" + endDate, yyyyMMdd).plusDays(1);
        PageRequest pageRequest = PageRequest.of(page, 15);
        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);

        Page<RefundFindAllDto.Response> response = refundRepository.findRefundAll(pageRequest, startLocalDate, endLocalDate, searchKeyword.isEmpty()
                ,isBusinessNumber,searchKeyword,refundStatus);

        int totalPage = response.getTotalPages();
        if(totalPage != 0){
            totalPage = totalPage -1;
        }

        return RefundTestPagingFindResponse.builder()
                .totalPage(totalPage)
                .refundFindResponseInterfaceList(response.getContent())
                .build();
    }
}
