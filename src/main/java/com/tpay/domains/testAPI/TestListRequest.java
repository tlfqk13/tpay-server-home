package com.tpay.domains.testAPI;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TestListRequest {
  private List<String> b_no;
}
