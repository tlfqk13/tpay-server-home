package com.tpay.domains.vat.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HometaxTailDto {
    private String totalCount;
    private String totalAmount;
    private String totalRefund;
    private String totalVat;

    @QueryProjection
    public HometaxTailDto(String totalCount, String totalAmount, String totalRefund, String totalVat) {
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
        this.totalRefund = totalRefund;
        this.totalVat = totalVat;
    }

    public static HometaxTailDto of(VatTotalResponseInterface vatTotalResponseInterface) {
        return HometaxTailDto.builder()
                .totalVat(vatTotalResponseInterface.getTotalVat())
                .totalAmount(vatTotalResponseInterface.getTotalAmount())
                .totalRefund(vatTotalResponseInterface.getTotalRefund())
                .totalCount(vatTotalResponseInterface.getTotalCount())
                .build();
    }
}
