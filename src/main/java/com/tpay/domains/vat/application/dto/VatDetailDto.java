package com.tpay.domains.vat.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

public class VatDetailDto {

    @Getter
    public static class Response{
        String purchaseSerialNumber;
        String saleDate;
        String finishDate; // 반출 일자 - 사후
        String takeOutConfirmNumber;
        String paymentFinishDate; // 송금 일자
        String refundAmount;
        String amount;
        String vat;
        String customerName;
        String customerNational;

        @QueryProjection
        public Response(String purchaseSerialNumber,String saleDate, String takeOutConfirmNumber,String refundAmount,
                        String amount, String vat, String customerName,String customerNational,
                        String finishDate, String paymentFinishDate){
            this.purchaseSerialNumber = purchaseSerialNumber;
            this.saleDate = saleDate;
            this.takeOutConfirmNumber = takeOutConfirmNumber;
            this.refundAmount = refundAmount;
            this.amount = amount;
            this.vat = vat;
            this.customerName = customerName;
            this.customerNational = customerNational;
            this.finishDate = finishDate;
            this.paymentFinishDate = paymentFinishDate;
        }
    }
}

