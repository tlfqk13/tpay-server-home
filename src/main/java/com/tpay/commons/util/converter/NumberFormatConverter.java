package com.tpay.commons.util.converter;


public class NumberFormatConverter {

    public static String addBarToBusinessNumber(String businessNumber) {
        return businessNumber.substring(0, 3) + "-" + businessNumber.substring(3, 5) + "-" + businessNumber.substring(5);
    }

    public static String addCommaToNumber(String targetNumber) {
        try {
            targetNumber = targetNumber.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        } catch (NullPointerException e) {
            targetNumber = "0";
        }
        return targetNumber;
    }
}
