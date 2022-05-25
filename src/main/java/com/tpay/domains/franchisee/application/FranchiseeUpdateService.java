package com.tpay.domains.franchisee.application;

import com.tpay.domains.franchisee.application.dto.FranchiseeUpdateDto;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseeUpdateService {

    private final FranchiseeFindService franchiseeFindService;

    @Transactional
    public boolean updatePopUp(Long franchiseeIndex) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        franchiseeEntity.popUpFalse();
        return false;
    }

    @Transactional
    public FranchiseeUpdateDto updateFranchisee(Long franchiseeIndex, FranchiseeUpdateDto franchiseeUpdateDto) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        FranchiseeEntity result = franchiseeEntity.updateFranchisee(franchiseeUpdateDto);
        return new FranchiseeUpdateDto(result);
    }
}
