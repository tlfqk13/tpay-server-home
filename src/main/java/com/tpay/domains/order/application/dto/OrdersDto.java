package com.tpay.domains.order.application.dto;

import lombok.*;

import java.util.List;

public class OrdersDto {
    @Getter
    @Builder
    public static class Response{
        List<List<String>> ordersDtoList;
    }

    @Getter
    public static class Request{
        String passportNumber;
    }
}
