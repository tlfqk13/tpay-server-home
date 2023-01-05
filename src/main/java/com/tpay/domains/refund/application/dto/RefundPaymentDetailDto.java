package com.tpay.domains.refund.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tpay.domains.customer.application.dto.CustomerPaymentType;
import lombok.Builder;
import lombok.Getter;

public class RefundPaymentDetailDto {

    @Getter
    @Builder
    public static class Response{
       private CustomerPaymentType customerPaymentType;
       private String customerBankName;
       private String customerAccountNumber;
       private String customerCreditNumber;

       @QueryProjection
        public Response(CustomerPaymentType customerPaymentType, String customerBankName,
                        String customerAccountNumber, String customerCreditNumber){
           this.customerPaymentType = customerPaymentType;
           this.customerBankName = customerBankName;
           this.customerAccountNumber = customerAccountNumber;
           this.customerCreditNumber = customerCreditNumber;
       }
    }
}
