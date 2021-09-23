package com.tpay.domains.order.application;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class SalesAnalysisServiceTest {

  @Test
  public void 날짜_변환_테스트() {

    String startDate = "2021-03-12";
    String endDate = "2021-04-10";

    LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
    LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

    System.out.println("LocalDate StartDate : " + start);
    System.out.println("LocalDate EndDate : " + end);

    LocalDateTime s = LocalDateTime.of(start, LocalTime.MIN);
    LocalDateTime e = LocalDateTime.of(end, LocalTime.MAX);

    System.out.println("LocalDateTime StartDate : " + s);
    System.out.println("LocalDateTime EndDate : " + e);
  }
}
