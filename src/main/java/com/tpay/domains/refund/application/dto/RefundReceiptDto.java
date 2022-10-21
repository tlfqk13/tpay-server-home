package com.tpay.domains.refund.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

public class RefundReceiptDto {
    @Getter
    public static class Response{
        private final String taxFreeStoreNumber;//사후면세판매자 지정번호
        private final String sellerName;//판매자
        private final String franchiseeName;//상호
        private final String businessNumber;//사업자번호
        private final String storeAddress;//주소
        private final String storeTelNumber;// 연락처

        private final LocalDateTime saleDate;//판매일
        //단가= 금액 - 부가가치세
        private final String totalAmount;//금액
        //판매총액 = 금액
        private final String totalVat;//부가가치세 (VAT)
        private final String totalRefund;//즉시환급상당액
        //결제금액 = 금액

        @QueryProjection
        public Response(String texFreeStoreNumber,LocalDateTime saleDate,String sellerName
                ,String franchiseeName,String businessNumber,String storeAddress
                ,String storeTelNumber,String totalAmount,String totalVat
        ,String totalRefund){

            this.taxFreeStoreNumber = texFreeStoreNumber;
            this.saleDate = saleDate;
            this.sellerName = sellerName;
            this.franchiseeName = franchiseeName;
            this.businessNumber = businessNumber;
            this.storeAddress = storeAddress;
            this.storeTelNumber = storeTelNumber;
            this.totalAmount = totalAmount;
            this.totalVat = totalVat;
            this.totalRefund = totalRefund;
        }
    }

    @Getter
    public static class Request{
        private String passportNumber;
    }
}

