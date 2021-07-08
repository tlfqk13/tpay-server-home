package com.tpay.commons.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class RegExUtils {

  public boolean validate(RegExType type, String value) {
    Pattern pattern = Pattern.compile(type.getPattern());
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }
}
