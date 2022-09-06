package com.tpay.domains.certifications.application.dto;


import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Data {
    private String bNo;
    private String bStt;
    private String bSttCd;
    private String taxType;
    private String taxTypeCd;
    private String endDt;
    private String utccYn;
    private String taxTypeChangeDt;
    private String invoiceApplyDt;
}
