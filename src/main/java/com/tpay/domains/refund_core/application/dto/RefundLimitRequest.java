package com.tpay.domains.refund_core.application.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
@AllArgsConstructor
public class RefundLimitRequest {
    private String name;
    private String passportNumber;
    private String nationality;
    private String totalAmount;
    private String saleDate;
    private Long franchiseeIndex;
    // TODO: 2022/11/11 한도조회 스켄 or 수기 
    private String method;

    public void updateNation(String nationality){
        this.nationality = nationality;
    }

    public void updatePassport(String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
