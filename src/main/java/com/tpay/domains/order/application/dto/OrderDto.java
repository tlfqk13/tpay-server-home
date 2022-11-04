package com.tpay.domains.order.application.dto;

import lombok.Value;

public class OrderDto {

    @Value
    public static class Request {
        Long customerIndex;
        String price;
        String refund;
    }

    @Value
    public static class Response {
        String purchaseSn;
    }
}
