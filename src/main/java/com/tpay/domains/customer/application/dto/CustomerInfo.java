package com.tpay.domains.customer.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CustomerInfo {
    private String nationality;
    private String passportNumber;
    private String name;
}
