package com.tpay.domains.order.application.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

public class CmsDto {

    @Getter
    public static class Response{
        Long franchiseeIndex;

        @QueryProjection
        public Response(Long franchiseeIndex){
            this.franchiseeIndex = franchiseeIndex;
        }
    }

    @Getter
    public static class Request{
        LocalDateTime year;
        LocalDateTime month;
    }
}
