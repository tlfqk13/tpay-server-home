package com.tpay.domains.van.domain.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VanOrderDetail {

    private String docId;
    private String shopNm;
    private String shopTypeCcd;
    private String purchsDate;
    private String totPurchsAmt;
    private String vat;
    private String totalRefund;
    private String rfndAvailableYn;
    private String earlyRfndYn;
    private String customsCleanceYn;
}
