package com.tpay.domains.external.application;

import com.tpay.domains.external.domain.ExternalRefundEntity;
import com.tpay.domains.external.domain.ExternalRefundStatus;
import com.tpay.domains.external.domain.ExternalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ExternalService {

    private final ExternalRepository externalRepository;

    @Transactional
    public ExternalRefundEntity save(Long franchiseeIndex, Long customerIndex){
        ExternalRefundEntity externalRefundEntity = new ExternalRefundEntity(franchiseeIndex,customerIndex, ExternalRefundStatus.SCAN);
        return externalRepository.save(externalRefundEntity);
    }
}
