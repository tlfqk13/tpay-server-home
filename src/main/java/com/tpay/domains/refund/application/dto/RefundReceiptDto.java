package com.tpay.domains.refund.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tpay.domains.customer.application.dto.DepartureStatus;
import com.tpay.domains.customer.domain.CustomerEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RefundReceiptDto {
    @Getter
    public static class Response {

        private final Long refundIndex;
        private final String barcodeS3Path; // 바코드 경로
        private final String purchaseSn; // 구매 일련번호
        private final boolean refundAfter; // 즉시, 사후환급 전표 구분
        private final String taxFreeStoreNumber;//사후면세판매자 지정번호
        private final String sellerName;//판매자
        private final String franchiseeName;//상호
        private final String businessNumber;//사업자번호
        private final String storeAddress;//주소
        private final String storeTelNumber;// 연락처
        private final String saleDate;//판매일
        //단가= 금액 - 부가가치세
        private final String totalAmount;//금액
        //판매총액 = 금액
        private final String totalVat;//부가가치세 (VAT)
        private final String totalRefund;//즉시환급상당액
        // 결제금액 = 금액
        private final String category;//상품명
        // 세액계 = totalVat
        private final String expireDate;// 반출유효기간
        private final String refundS3Path;

        @QueryProjection
        public Response(Long refundIndex, String barcodeS3Path, String purchaseSn, boolean refundAfter
                , String texFreeStoreNumber, String saleDate, String sellerName
                , String franchiseeName, String businessNumber, String storeAddress
                , String storeTelNumber, String totalAmount, String totalVat
                , String totalRefund, String category, LocalDateTime expireDate
                , String refundS3Path) {

            this.refundIndex = refundIndex;
            this.barcodeS3Path = barcodeS3Path;
            this.purchaseSn = purchaseSn;
            this.refundAfter = refundAfter;
            this.taxFreeStoreNumber = texFreeStoreNumber;
            this.saleDate = saleDate.replace("T", " ");
            this.sellerName = sellerName;
            this.franchiseeName = franchiseeName;
            this.businessNumber = businessNumber;
            this.storeAddress = storeAddress;
            this.storeTelNumber = storeTelNumber;
            this.totalAmount = totalAmount;
            this.totalVat = totalVat;
            this.totalRefund = totalRefund;
            this.category = category;
            this.expireDate = expireDate.plusMonths(3).toString().substring(0, 10);
            this.refundS3Path = refundS3Path;
        }
    }

    @Getter
    @Builder
    public static class ResponseCustomer {
        private String customerNation;
        private String departureDate;
        private DepartureStatus departureStatus;
        private boolean isRead;
        private boolean isRegister;
        private boolean isReceiptUpload;
        private boolean uploadRequired;
        // 영수증 업로드 유무
        // 2022/10/21 사후 환급 전표만 출력.

        public static ResponseCustomer of(CustomerEntity customerEntity) {
            return RefundReceiptDto.ResponseCustomer.builder()
                    .customerNation(customerEntity.getNation())
                    .departureDate(customerEntity.getDepartureDate() != null ? customerEntity.getDepartureDate() : null)
                    .departureStatus(customerEntity.getDepartureStatus())
                    .isRegister(customerEntity.getIsRegister())
                    .isRead(customerEntity.getIsRead())
                    .isReceiptUpload(customerEntity.getIsReceiptUpload())
                    .uploadRequired(customerEntity.getNation().equals("KOR"))
                    .build();
        }
    }

    @Getter
    public static class RefundReceiptUploadListDto {
        Long refundIndex;
        String franchiseeName;
        String category;//상품명
        String price;//금액
        String refund;//즉시환급상당액
        String refundS3Path;


        public RefundReceiptUploadListDto(RefundReceiptDto.Response response) {
            this.refundIndex = response.getRefundIndex();
            this.franchiseeName = response.getFranchiseeName();
            this.category = response.getCategory();
            this.price = response.getTotalAmount();
            this.refund = response.getTotalRefund();
            this.refundS3Path = response.getRefundS3Path();
        }
    }

    @Getter
    public static class Request {
        private String passportNumber;
        private String departureDate;
        // 2022/10/21 사후 환급 전표만 출력.
        private boolean refundAfter;
        private boolean latest;
    }

    private static String replaceSaleDate(String saleDate) {
        return saleDate.toString().replace("T", " ");
    }
}

