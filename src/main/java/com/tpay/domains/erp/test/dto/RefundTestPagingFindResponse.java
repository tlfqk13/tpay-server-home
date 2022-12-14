package com.tpay.domains.erp.test.dto;


import com.tpay.domains.refund.application.dto.RefundFindAllDto;
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
