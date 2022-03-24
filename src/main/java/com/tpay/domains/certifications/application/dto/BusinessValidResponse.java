package com.tpay.domains.certifications.application.dto;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessValidResponse {
    private String request_cnt;
//  private String match_cnt;
//  private String statusCode;
//  private List<Data> data;
}
