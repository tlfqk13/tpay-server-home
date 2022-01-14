package com.tpay.domains.certifications.application.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class BusinessValidApiRequest {
  String[] b_no;
}
