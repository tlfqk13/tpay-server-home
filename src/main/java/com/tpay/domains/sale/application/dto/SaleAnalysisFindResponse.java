package com.tpay.domains.sale.application.dto;


import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleAnalysisFindResponse {
    String date;
    Integer totalAmount;
    Integer totalRefund;
    Integer actualAmount;
    Integer saleCount;
    Integer cancelCount;

    public SaleAnalysisFindResponse(SaleAnalysisFindResponseInterface saleAnalysisFindResponseInterface) {
        this.date =  saleAnalysisFindResponseInterface.getDate();
        this.totalAmount = Math.round(saleAnalysisFindResponseInterface.getTotalAmount());
        this.totalRefund = Math.round(saleAnalysisFindResponseInterface.getTotalRefund());
        this.actualAmount = Math.round(saleAnalysisFindResponseInterface.getActualAmount());
        this.saleCount = Math.round(saleAnalysisFindResponseInterface.getSaleCount());
        this.cancelCount = Math.round(saleAnalysisFindResponseInterface.getCancelCount());
    }
}
