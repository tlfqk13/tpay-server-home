package com.tpay.domains.sale.application.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleAnalysisFindResponse {
    LocalDate date;
    Integer totalAmount;
    Integer totalRefund;
    Integer actualAmount;
    Integer saleCount;
    Integer cancelCount;

    public SaleAnalysisFindResponse(SaleAnalysisFindResponseInterface saleAnalysisFindResponseInterface) {
        this.date =  saleAnalysisFindResponseInterface.getFormatDate();
        this.totalAmount = Math.round(saleAnalysisFindResponseInterface.getTotalAmount());
        this.totalRefund = Math.round(saleAnalysisFindResponseInterface.getTotalRefund());
        this.actualAmount = Math.round(saleAnalysisFindResponseInterface.getActualAmount());
        this.saleCount = Math.round(saleAnalysisFindResponseInterface.getSaleCount());
        this.cancelCount = Math.round(saleAnalysisFindResponseInterface.getCancelCount());
    }
}
