package com.tpay.domains.barcode.application.dto;

import lombok.Getter;

@Getter
public class BarcodeCreateRequest {
  private String passportNumber;
  private String deduction;
}
