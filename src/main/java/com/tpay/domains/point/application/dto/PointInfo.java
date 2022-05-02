package com.tpay.domains.point.application.dto;

import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointStatus;
import com.tpay.domains.point_scheduled.domain.PointScheduledEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointInfo {
    private String datetime;
    private String totalAmount;
    private Long value;
    private PointStatus pointStatus;


    public PointInfo(PointScheduledEntity pointScheduledEntity) {
        this.datetime = pointScheduledEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss"));
        this.totalAmount = pointScheduledEntity.getOrderEntity().getTotalAmount();
        this.value = pointScheduledEntity.getValue();
        this.pointStatus = pointScheduledEntity.getPointStatus();
    }

    public PointInfo(PointEntity pointEntity) {
        this.datetime = pointEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss"));
        this.value = pointEntity.getChange();
        this.pointStatus = pointEntity.getPointStatus();
    }
}
