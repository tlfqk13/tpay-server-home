package com.tpay.domains.vat.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

public class VatTotalDto {

    @Getter
    @Builder
    public static class Response{
        String totalCount;
        String totalAmount;
        String totalVat;
        String totalRefund;
        String totalCommission;
        String totalSupply;

        @QueryProjection
        public Response(String totalCount, String totalAmount, String totalVat, String totalRefund, String totalCommission,String totalSupply){
            this.totalCount = totalCount;
            this.totalAmount = totalAmount;
            this.totalVat = totalVat;
            this.totalRefund = totalRefund;
            this.totalCommission = totalCommission;
            this.totalSupply = totalSupply;
        }
    }

}
