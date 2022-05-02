package com.tpay.domains.vat.application.dto;


import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VatDetailResponse {
    private List<String> vatDetailResponsePersonalInfoList;
    private List<String> vatDetailResponseTotalInfoList;
    private List<List<String>> vatDetailResponseDetailInfoListList;
}
