package com.tpay.domains.refund.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tpay.domains.customer.application.dto.CustomerPaymentType;
import lombok.Builder;
import lombok.Getter;

public class RefundPaymentInfoDto {

    @Getter
    @Builder
    public static class Response{
       private CustomerPaymentType customerPaymentType;
       private String paymentInfo;

       @QueryProjection
        public Response(CustomerPaymentType customerPaymentType, String paymentInfo){
           this.customerPaymentType = customerPaymentType;
           this.paymentInfo = paymentInfo;
       }
    }
}
