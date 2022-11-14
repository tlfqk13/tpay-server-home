package com.tpay.commons.custom;

import org.springframework.stereotype.Service;

@Service
public class RefundRateCondition {
    private String initVAT(String price) {
        return String.valueOf(Math.floorDiv(Long.parseLong(price), 11));
    }
    public String nonUpdateUserTotalRefund(String price) {

        String totalVat = this.initVAT(price);
        double vat = Double.parseDouble(totalVat);
        double totalRefund = Math.floor(((vat*70)/100)/100)*100;
        // TODO: 2022/08/09 합계 보정을 위해
        int amount = Integer.parseInt(price);
        double actualAmountResult = (Math.ceil( (amount - totalRefund) /100) * 100);
        int totalRefundResult = (int) (amount - actualAmountResult);

        return Integer.toString(totalRefundResult);
    }
}
