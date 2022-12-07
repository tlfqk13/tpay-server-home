package com.tpay.domains.point.application.dto;


import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminPointResponse {

    private int totalPage;
    private List<AdminPointInfo> adminPointInfoList;
}
