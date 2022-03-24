package com.tpay.domains.franchisee.application.dto.vat;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeVatResponse {
    private String totalAmount;
    private String totalCount;
    private String totalVat;
    private String totalSupply;
}
