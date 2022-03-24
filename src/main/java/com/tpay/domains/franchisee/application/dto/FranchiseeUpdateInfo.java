package com.tpay.domains.franchisee.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeUpdateInfo {
    private String email;
    private String storeNumber;
}
