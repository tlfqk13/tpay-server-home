package com.tpay.commons.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushType {
    TOKEN("token"),
    TOPIC("topic");

    private final String type;


    @Override
    public String toString() {
        return this.getType();
    }
}
