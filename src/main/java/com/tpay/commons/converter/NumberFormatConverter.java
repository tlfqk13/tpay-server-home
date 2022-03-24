package com.tpay.commons.converter;


import org.springframework.stereotype.Component;

@Component
public class NumberFormatConverter {

    public String addBarToBusinessNumber(String businessNumber) {
        return businessNumber.substring(0, 3) + "-" + businessNumber.substring(3, 5) + "-" + businessNumber.substring(5);
    }

    public String addCommaToNumber(String targetNumber) {
        try {
            targetNumber = targetNumber.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        } catch (NullPointerException e) {
            targetNumber = "0";
        }
        return targetNumber;
    }
}
