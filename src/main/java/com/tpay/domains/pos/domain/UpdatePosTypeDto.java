package com.tpay.domains.pos.domain;


import lombok.*;

public class UpdatePosTypeDto {

    @Getter
    public static class Request {
        private Boolean isConnected;
        private String posType;
    }
}
