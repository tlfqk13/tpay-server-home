package com.tpay.domains.certifications.application.dto;


import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BusinessValidRequestList {
  private List<String> b_no;
}
