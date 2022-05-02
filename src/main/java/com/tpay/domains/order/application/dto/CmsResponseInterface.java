package com.tpay.domains.order.application.dto;

public interface CmsResponseInterface {
    Long getFranchiseeIndex();

    String getTotalCount();

    String getTotalAmount();

    String getTotalVat();

    String getTotalCommission();
}
