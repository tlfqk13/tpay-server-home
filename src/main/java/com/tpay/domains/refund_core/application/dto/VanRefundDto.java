package com.tpay.domains.refund_core.application.dto;

import lombok.Getter;
import lombok.Value;

import java.util.List;

public class VanRefundDto {

    @Value
    public static class Request {
        String paymentMethod;
        String paymentInfo;
        String paymentBrand;
        List<VanDocumentDto.Request> vanDocuments;
    }
}
