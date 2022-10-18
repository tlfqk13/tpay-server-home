package com.tpay.domains.refund.application;

import com.tpay.commons.aria.PassportNumberEncryptService;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.refund.application.dto.RefundReceiptDto;
import com.tpay.domains.refund.domain.RefundEntity;
import com.tpay.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class RefundReceiptFindService {

    private final RefundRepository refundRepository;
    private final PassportNumberEncryptService encryptService;

    public RefundEntity findById(Long refundIndex) {

        return refundRepository.findById(refundIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Invalid RefundIndex"));
    }

    public List<RefundReceiptDto.Response> findRefundReceiptDetail(String passportNumber){
        log.trace(" @@ passportNumber = {}", passportNumber);
        String encryptPassportNumber = encryptService.encrypt(passportNumber);
        List<RefundReceiptDto.Response> response = refundRepository.findRefundReceipt(encryptPassportNumber);
        return response;
    }
}
