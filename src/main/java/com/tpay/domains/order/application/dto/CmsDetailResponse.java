package com.tpay.domains.order.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CmsDetailResponse {
    private List<String> commissionInfoList;
    private List<String> customerInfoList;
    private String downloadLink;
}
