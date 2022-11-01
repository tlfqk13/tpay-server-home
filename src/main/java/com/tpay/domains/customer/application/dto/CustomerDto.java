package com.tpay.domains.customer.application.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response{
        private String passportNumber;
        private String name;
        private String email;
        private String phoneNumber;
        private CustomerPaymentType customerPaymentType;
        private String creditCardNumber;
        private String bankName;
        private String accountNumber;
        private String nation;
    }

    @Getter
    public static class Request{
        private String passportNumber;
        private String name;
        private String email;
        private String phoneNumber;
        private CustomerPaymentType customerPaymentType;
        private String creditCardNumber;
        private String bankName;
        private String accountNumber;
    }
}
