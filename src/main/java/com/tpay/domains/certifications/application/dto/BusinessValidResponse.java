package com.tpay.domains.certifications.application.dto;


import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessValidResponse {
  private String request_cnt;
//  private String matchnt;
//  private String statusCode;
//  private List<Data> data;
}
