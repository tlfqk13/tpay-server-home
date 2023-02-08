package com.tpay.domains.refund.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPassportInfoException;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.customer.domain.CustomerRepository;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class RefundReceiptFindService {

    private final RefundRepository refundRepository;
    private final PassportNumberEncryptService encryptService;
    private final CustomerRepository customerRepository;

    public RefundEntity findById(Long refundIndex) {

        return refundRepository.findById(refundIndex)
                .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid RefundIndex"));
    }

    @Transactional
    public List<RefundReceiptDto.Response> findRefundReceiptDetail(RefundReceiptDto.Request request) {

        CustomerEntity customerEntity = getCustomerEntity(request);

        List<RefundReceiptDto.Response> response;
        // 최신순, 과거순
        response = refundRepository.findRefundReceipt(customerEntity.getPassportNumber(), request.isRefundAfter());

        if (!"KOR".equals(customerEntity.getNation())) {
            response = response.stream().filter(r -> Integer.parseInt(r.getTotalRefund()) >= 75000).collect(Collectors.toList());
        }

        if (request.isLatest()) {
            response = response.stream().sorted(Comparator.comparing(RefundReceiptDto.Response::getSaleDate).reversed()).collect(Collectors.toList());
        }

        customerEntity.updateIsRead();

        return response;
    }

    public List<RefundReceiptDto.Response> downloadsRefundReceiptDetail(RefundReceiptDto.Request request) {

        CustomerEntity customerEntity = getCustomerEntity(request);

        List<RefundReceiptDto.Response> response;
        // 최신순, 과거순
        response = refundRepository.downloadsRefundReceipt(customerEntity.getPassportNumber(), request.isRefundAfter());

        if (request.isLatest()) {
            response = response.stream().sorted(Comparator.comparing(RefundReceiptDto.Response::getSaleDate).reversed()).collect(Collectors.toList());
        }
        return response;
    }

    public RefundReceiptDto.ResponseCustomer findCustomer(RefundReceiptDto.Request request) {
        CustomerEntity customerEntity = getCustomerEntity(request);
        return RefundReceiptDto.ResponseCustomer.of(customerEntity);
    }

    @Transactional
    public RefundReceiptDto.ResponseCustomer updateDepartureDate(RefundReceiptDto.Request request) {
        CustomerEntity customerEntity = getCustomerEntity(request);
        customerEntity.updateDepartureDate(request.getDepartureDate());
        return RefundReceiptDto.ResponseCustomer.of(customerEntity);
    }

    private CustomerEntity getCustomerEntity(RefundReceiptDto.Request request) {
        String encryptPassportNumber = encryptService.encrypt(request.getPassportNumber());
        log.trace(" @@ encryptPassportNumber = {}", encryptPassportNumber);
        return customerRepository.findByPassportNumber(encryptPassportNumber)
                .orElseThrow(() -> new InvalidPassportInfoException(ExceptionState.INVALID_PASSPORT_INFO, "여권 조회 실패"));
    }

    public List<RefundReceiptDto.RefundReceiptUploadListDto> findRefundReceiptUploadList(RefundReceiptDto.Request request) {
        List<RefundReceiptDto.Response> refundReceiptDetail = findRefundReceiptDetail(request);
        return refundReceiptDetail.stream()
                .map(RefundReceiptDto.RefundReceiptUploadListDto::new)
                .distinct()
                .collect(Collectors.toList());
    }
}
