package com.tpay.domains.order.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderDtoInfo {

    private String docId;
    private String shopNm;
    private String shopTypeCcd;
    private String purchsDate;
    private String totPurchsAmt;
    private String vat;
    private String rfndAvailableYn;
    private String earlyRfndYn;
    private String customsCleanceYn;
}
