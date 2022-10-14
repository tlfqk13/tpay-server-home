package com.tpay.domains.refund_core.application.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
public class RefundResponse {
    private String responseCode; // '0000': 정상
    private String message; // 환급 통신 에러일 경우 에러 메시지 반환

    // 환급 취소 로직 요청 필드로 각 서비스별 데이터베이스 저장 필요
    private String purchaseSequenceNumber;
    private String takeoutNumber;

    // 환급 한도 조회 반환 필드
    private String beforeDeduction;
    private String afterDeduction;

    private String passportNumber;
    private String nationality;

    private Long customerIndex;

    public RefundResponse addCustomerInfo(Long customerIndex) {
        this.customerIndex = customerIndex;
        return this;
    }
}
