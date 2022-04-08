package com.tpay.commons.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    public static LocalDate stringToLocalDate(String string) {
        string = string.substring(0, string.lastIndexOf(".")); // 문자열을 일정하게 짜름

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDate.parse(string, formatter);
    }


}
