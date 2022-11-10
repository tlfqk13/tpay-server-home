package com.tpay.domains.customer.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerDto {

    @Getter
    @Builder
    public static class Response{
        private String passportNumber;
        private String name;
        private String email;
        private CustomerPaymentType customerPaymentType;
        private String creditCardNumber;
        private String bankName;
        private String accountNumber;
        private String nation;
        @QueryProjection
        public Response(String passportNumber, String name,String email,
                        CustomerPaymentType customerPaymentType, String creditCardNumber, String bankName,
                        String accountNumber, String nation){
            this.passportNumber = passportNumber;
            this.name = name;
            this.email = email;
            this.customerPaymentType = customerPaymentType;
            this.creditCardNumber = creditCardNumber;
            this.bankName = bankName;
            this.accountNumber = accountNumber;
            this.nation = nation;
        }
    }

    @Getter
    public static class Request{
        private String passportNumber;
        private String name;
        private String email;
        private CustomerPaymentType customerPaymentType;
        private String creditCardNumber;
        private String bankName;
        private String accountNumber;
    }
}
