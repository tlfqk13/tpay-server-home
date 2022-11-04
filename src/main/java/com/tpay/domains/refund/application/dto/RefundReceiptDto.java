package com.tpay.domains.refund.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

public class RefundReceiptDto {
    @Getter
    public static class Response{
        private final boolean refundAfter; // 즉시, 사후환급 전표 구분
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
        // 결제금액 = 금액
        private final String administrativeCharge;// 제비용 = (즉시환급상당액 - 포인트) = 우리가 벌어가는거
        // 세액계 = totalVat
        private final String expireDate;// 반출유효기간

        @QueryProjection
        public Response(boolean refundAfter
                ,String texFreeStoreNumber,LocalDateTime saleDate,String sellerName
                ,String franchiseeName,String businessNumber,String storeAddress
                ,String storeTelNumber,String totalAmount,String totalVat
                ,String totalRefund, String administrativeCharge, String expireDate){

            this.refundAfter = refundAfter;
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
            this.administrativeCharge = administrativeCharge;
            this.expireDate = expireDate;
        }
    }

    @Getter
    public static class Request{
        private String passportNumber;
        // TODO: 2022/10/21 사후 환급 전표만 출력.
        private boolean refundAfter;
        private boolean latest;
    }
}

