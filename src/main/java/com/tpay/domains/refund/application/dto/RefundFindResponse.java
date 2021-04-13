package com.tpay.domains.refund.application.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefundFindResponse {

    private String orderNumber;
    private String saleDate;
    private String nation;
    private String passportNumber;
    private String name;

    private String totalAmount;
    private String totalVat;
    private String saleAmount;
    private String point;


}
