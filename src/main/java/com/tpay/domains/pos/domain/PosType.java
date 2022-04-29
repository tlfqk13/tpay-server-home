package com.tpay.domains.pos.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PosType {
    INIT("사용중인 포스기 선택"),
    P001("그로잉 세일즈"),
    P002("OK 포스"),
    P003("포스뱅크");

    private final String posName;

}
