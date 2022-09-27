package com.tpay.domains.order.application.dto;

import lombok.Value;

public class OrderDto {

    @Value
    public static class Request {
        Long customerIdx;
        String price;
    }

    @Value
    public static class Response {
        String purchaseSn;
    }
}
