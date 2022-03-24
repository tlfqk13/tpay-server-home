package com.tpay.domains.point.application.dto;

import com.tpay.domains.point.domain.PointStatus;
import lombok.Getter;


@Getter
public class PointUpdateRequest {
    private Boolean isRead;
    private PointStatus pointStatus;
}
