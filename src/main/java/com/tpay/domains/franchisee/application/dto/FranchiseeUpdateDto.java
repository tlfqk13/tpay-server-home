package com.tpay.domains.franchisee.application.dto;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.Getter;

@Getter
public class FranchiseeUpdateDto {
    private final String storeNumber;
    private final String email;

    public FranchiseeUpdateDto(FranchiseeEntity franchiseeEntity) {
        this.storeNumber = franchiseeEntity.getStoreNumber();
        this.email = franchiseeEntity.getEmail();
    }
}
