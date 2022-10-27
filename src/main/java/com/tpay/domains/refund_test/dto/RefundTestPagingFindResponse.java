package com.tpay.domains.refund_test.dto;


import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefundTestPagingFindResponse {
    int totalPage;
    private List<RefundFindAllDto.Response> refundFindResponseInterfaceList;
}
