package com.tpay.domains.erp.deploy.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchAllResponse {
    private List<String> searchAllBusinessNumberList;
    private List<String> searchAllStoreNameList;
}
