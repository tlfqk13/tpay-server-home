package com.tpay.domains.van.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class VanOrdersDto {
    @Getter
    @Builder
    public static class Response{
        List<VanOrderDetail> vanOrderDetails;
    }

    @Getter
    public static class Request{
        String encryptPassportNumber;
    }
}
