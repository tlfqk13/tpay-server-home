package com.tpay.domains.vat.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VatResponse {
    private String totalAmount;
    private String totalCount;
    private String totalVat;
    private String totalSupply;


    // 의도치않은 0 인스턴스 방지용 생성자
    public VatResponse(Boolean isEmpty){
        if(isEmpty) {
            this.totalAmount = "0";
            this.totalCount = "0";
            this.totalVat = "0";
            this.totalSupply = "0";
        }
    }
}
