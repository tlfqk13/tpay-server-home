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

    public List<RefundReceiptDto.Response> findRefundReceiptDetail(RefundReceiptDto.Request request){

        String encryptPassportNumber = encryptService.encrypt(request.getPassportNumber());

        CustomerEntity customerEntity = customerRepository.findByPassportNumber(encryptPassportNumber)
                .orElseThrow(()->new InvalidPassportInfoException(ExceptionState.INVALID_PASSPORT_INFO, "여권 조회 실패"));

        List<RefundReceiptDto.Response> response;
        // 최신순, 과거순
        response = refundRepository.findRefundReceipt(customerEntity.getPassportNumber(),request.isRefundAfter());

        if(request.isLatest()){
            response = response.stream().sorted(Comparator.comparing(RefundReceiptDto.Response::getSaleDate).reversed()).collect(Collectors.toList());
        }
        return response;
    }
}
