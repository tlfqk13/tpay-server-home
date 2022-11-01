package com.tpay.domains.vat.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

public class VatDetailDto {

    @Getter
    public static class Response{
        String purchaseSerialNumber;
        String saleDate;
        String takeOutConfirmNumber;
        String refundAmount;
        String amount;
        String vat;
        String customerName;
        String customerNational;

        @QueryProjection
        public Response(String purchaseSerialNumber,String saleDate, String takeOutConfirmNumber,String refundAmount,
                        String amount, String vat, String customerName,String customerNational ){
            this.purchaseSerialNumber = purchaseSerialNumber;
            this.saleDate = saleDate;
            this.takeOutConfirmNumber = takeOutConfirmNumber;
            this.refundAmount = refundAmount;
            this.amount = amount;
            this.vat = vat;
            this.customerName = customerName;
            this.customerNational = customerNational;
        }
    }
}

