package com.tpay.domains.franchisee.application.dto.vat;


public interface FranchiseeVatReportResponseInterface {
    Long getFranchiseeIndex();

    String getTotalAmount();

    String getTotalCount();

    String getTotalVat();

    String getTotalSupply();
}
