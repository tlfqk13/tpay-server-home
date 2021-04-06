package com.tpay.domains.certifications.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class CertificationInfo {
  private Long code;
  private String message;
  private Response response;

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @ToString
  public static class Response {
    private Long birth;
    private String birthday;
    private boolean certified;
    private Long certified_at;
    private boolean foreigner;
    private String gender;
    private String imp_uid;
    private String merchant_uid;
    private String name;
    private String phone;
    private String origin;
    private String pg_provider;
    private String pg_tid;
    private String unique_in_site;
    private String unique_key;
  }
}
