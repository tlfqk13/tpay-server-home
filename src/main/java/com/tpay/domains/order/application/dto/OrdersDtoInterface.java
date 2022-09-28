package com.tpay.domains.order.application.dto;

public interface OrdersDtoInterface {

    String getDocId();
    String getShopNm();
    String getShopTypeCcd();
    String getPurchsDate();
    String getTotPurchsAmt();
    String getVat();
    String getTotalRefund();
    String getRfndAvailableYn();
    String getEarlyRfndYn();
    String getCustomsCleanceYn();

}
