package com.tpay.domains.franchisee.application.dto;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.Getter;

@Getter
public class FranchiseeUpdateDtoResponse {
    private final String storeNumber;
    private final String email;

    public FranchiseeUpdateDtoResponse(FranchiseeEntity franchiseeEntity) {
        this.storeNumber = franchiseeEntity.getStoreNumber();
        this.email = franchiseeEntity.getEmail();
    }
}
