package com.tpay.domains.certifications.application.dto;


import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class BusinessValidResponse {
  private String requestCnt;
  private String matchCnt;
  private String statusCode;
  private List<Data> data;
}
