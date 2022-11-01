package com.tpay.domains.refund.application.dto;


import com.tpay.domains.refund_test.dto.RefundFindAllDto;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefundPagingFindResponse {
    int totalPage;
    private List<RefundFindAllDto.Response> refundFindResponseInterfaceList;
}
