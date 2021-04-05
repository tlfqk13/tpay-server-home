package com.tpay.commons.custom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomValue {

  public static String IAMPORT_API_KEY;
  public static String IAMPORT_SECRET;

  @Value("${custom.iamport.api-key}")
  public void setIamportApiKey(String iamportApiKey) {
    IAMPORT_API_KEY = iamportApiKey;
  }

  @Value("${custom.iamport.secret}")
  public void setIamportSecret(String iamportSecret) {
    IAMPORT_SECRET = iamportSecret;
  }
}
