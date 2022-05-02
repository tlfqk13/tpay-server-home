package com.tpay.domains.order.application.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CmsResponse {
    private String totalCount;
    private String totalAmount;
    private String totalVat;
    private String totalCommission;

    public CmsResponse(Boolean isEmpty){
        if(isEmpty){
            this.totalCount = "0";
            this.totalAmount = "0";
            this.totalVat = "0";
            this.totalCommission = "0";
        }
    }
}
