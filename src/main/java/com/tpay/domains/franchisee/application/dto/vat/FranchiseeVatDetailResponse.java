package com.tpay.domains.franchisee.application.dto.vat;


import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeVatDetailResponse {
    private List<Object> vatDetailResponsePersonalInfoList;
    private List<Object> vatDetailResponseTotalInfoList;
    private List<List<Object>> vatDetailResponseDetailInfoListList;
}
