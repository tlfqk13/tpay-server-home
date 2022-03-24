package com.tpay.domains.point.application.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointFindResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<PointInfo> pointInfoList;
}
