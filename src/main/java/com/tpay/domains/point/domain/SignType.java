package com.tpay.domains.point.domain;

public enum SignType {
  POSITIVE("+"),
  NEGATIVE("-");

  private final String type;

  SignType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }
}
