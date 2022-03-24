package com.tpay.domains.refund.application.dto;


import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RefundCustomerDateRequest {
    private String orderCheck;
    private LocalDate startDate;
    private LocalDate endDate;
}
