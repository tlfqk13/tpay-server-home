package com.tpay.domains.point.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointFindResponse {
    private String startDate;
    private String endDate;
    private List<PointInfo> pointInfoList;
}
