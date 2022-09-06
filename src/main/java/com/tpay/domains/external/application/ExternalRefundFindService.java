package com.tpay.domains.external.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidExternalRefundIndexException;
import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalRefundFindService {

    private final ExternalRepository externalRepository;


    public ExternalRefundEntity findById(Long externalRefundIndex) {
        return externalRepository.findById(externalRefundIndex)
            .orElseThrow(() -> new InvalidExternalRefundIndexException(ExceptionState.INVALID_EXTERNAL_REFUND_INDEX, "Invalid External Refund Id"));

    }
}
