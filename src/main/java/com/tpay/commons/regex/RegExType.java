package com.tpay.commons.regex;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegExType {
    BUSINESS_NUMBER("^\\d{3}-\\d{2}-\\d{5}$"),
    PASSWORD("^[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>@\\#$%&\\\\\\=\\(\\'\\A-Xa-z0-9+]{4,20}$"),
    EMAIL("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$"),
    PASSPORT("([a-zA-Z]{1}|[a-zA-Z]{2})\\d{8}");

    private final String pattern;
}
