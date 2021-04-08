package com.tpay.domains.refund.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundRequestProductInfoList {

  private String productName;
  private String productPrice;
  private String productQuantity;
  private String productSequenceNumber;
  private String productCode;
  private String salePrice;
  private String indVAT;
  private String indIct;
  private String indEdut;
  private String indStr;
}
