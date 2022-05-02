package com.tpay.domains.external.domain;

public enum ExternalRefundStatus {
    SCAN,
    APPROVE,
    CANCEL, // External에서 만료되어도 CANCEL
    CONFIRMED
}
