package com.tpay.domains.vat.application.dto;


public interface VatReportResponseInterface {
    Long getFranchiseeIndex();

    String getTotalAmount();

    String getTotalCount();

    String getTotalVat();

    String getTotalSupply();
}
