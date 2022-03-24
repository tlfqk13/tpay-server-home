package com.tpay.domains.franchisee_upload.application;


import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FranchiseeUploadFindService {
    private final FranchiseeUploadRepository franchiseeUploadRepository;

    public FranchiseeUploadEntity findByFranchiseeIndex(Long franchiseeId) {
        Optional<FranchiseeUploadEntity> optionalFranchiseeUploadEntity = franchiseeUploadRepository.findByFranchiseeIndex(franchiseeId);

        if (optionalFranchiseeUploadEntity.isEmpty()) {
            return FranchiseeUploadEntity.builder().build();
        }
        return optionalFranchiseeUploadEntity.get();
    }
}
