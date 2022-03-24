package com.tpay.domains.point.application.dto;

import com.tpay.domains.point.domain.PointStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointInfo {
    private String datetime;
    private String totalAmount;
    private Long value;
    private PointStatus pointStatus;
}
