package com.tpay.commons.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

  @Override
  public LocalDate convert(String source) {
    try {
      return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    } catch (Exception e) {
      throw new IllegalArgumentException("Passing date format must be yyyy-MM-dd");
    }
  }
}
