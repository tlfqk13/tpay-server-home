package com.tpay.domains.order.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

public class CmsDetailDto {
    @Getter
    public static class Response {
        String sellerName;
        String bankName;
        String accountNumber;
        String withdrawalDate;

        @QueryProjection
        public Response(String sellerName, String bankName, String accountNumber, String withdrawalDate){

            this.sellerName = sellerName;
            this.bankName = bankName;
            this.accountNumber = accountNumber;
            this.withdrawalDate = withdrawalDate;
        }
    }
}
