package com.tpay.commons.custom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomValue {

  public static String IAMPORT_API_KEY;
  public static String IAMPORT_SECRET;
  public static String REFUND_SERVER;
  public static String APPLICATION_CODE;
  public static String BUSINESS_VALID_ACCESS;
  public static String BANK_VALID_ID;
  public static String BANK_VALID_SECRET;

  @Value("${custom.iamport.api-key}")
  public void setIamportApiKey(String iamportApiKey) {
    IAMPORT_API_KEY = iamportApiKey;
  }

  @Value("${custom.iamport.secret}")
  public void setIamportSecret(String iamportSecret) {
    IAMPORT_SECRET = iamportSecret;
  }

  @Value("${custom.refund.server}")
  public void setRefundServer(String refundServer) {
    REFUND_SERVER = refundServer;
  }

  @Value("${custom.refund.application.code}")
  public void setApplicationCode(String applicationCode) {
    APPLICATION_CODE = applicationCode;
  }

  @Value("${custom.upload.business}")
  public void setBusinessValidAccess(String businessValidAccess) { BUSINESS_VALID_ACCESS = businessValidAccess;}

  @Value("${custom.upload.bank.client-id}")
  public void setBankValidId(String bankValidId) {BANK_VALID_ID = bankValidId;}

  @Value("${custom.upload.bank.client-secret}")
  public void setBankValidSecret(String bankValidSecret) {BANK_VALID_SECRET = bankValidSecret;}
}
