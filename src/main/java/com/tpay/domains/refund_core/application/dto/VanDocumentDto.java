package com.tpay.domains.refund_core.application.dto;

import lombok.Getter;
import lombok.Value;

public class VanDocumentDto {

    @Value
    public static class Request {
        String docId;
        String refundAmount;
    }
}
