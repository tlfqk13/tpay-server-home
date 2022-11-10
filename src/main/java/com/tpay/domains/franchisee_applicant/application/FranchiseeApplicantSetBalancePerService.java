package com.tpay.domains.franchisee_applicant.application;


import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class FranchiseeApplicantSetBalancePerService {

    private final FranchiseeFindService franchiseeFindService;

    @Transactional
    public double updateBalancePercentage(Long franchiseeApplicantIndex, FranchiseeApplicantUpdateDto.balancePercentageRequest request) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeApplicantIndex);
        return franchiseeEntity.updateBalancePercentage(request.getBalancePercentage());
    }
}

