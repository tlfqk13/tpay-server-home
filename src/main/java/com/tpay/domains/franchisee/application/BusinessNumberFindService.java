package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.BusinessNumberFindRequest;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessNumberFindService {

    private final FranchiseeRepository franchiseeRepository;

    @Transactional
    public String findBusinessNumber(BusinessNumberFindRequest businessNumberFindRequest) {
        FranchiseeEntity franchiseeEntity =
            franchiseeRepository
                .findBySellerNameAndStoreTel(
                    businessNumberFindRequest.getName(), businessNumberFindRequest.getPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Seller Name or Store Tel"));

        return franchiseeEntity.getBusinessNumber();
    }
}
