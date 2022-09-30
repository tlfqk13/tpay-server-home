package com.tpay.domains.refund.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@Builder
public class RefundConfirmDto {
    String purchaseSn;
    String encryptPassportNum;
    String passportName;
    String passportNation;
    String tkOutConfNum;
    String totalAmount;
}
