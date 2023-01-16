package com.tpay.domains.van.domain.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VanRefundAfterBaseDto {
    String cusCode;
    String locaCode;
    String kioskBsnmCode;
    String kioskCode;
    String refundAfterMethod;
    String barcode;
}
