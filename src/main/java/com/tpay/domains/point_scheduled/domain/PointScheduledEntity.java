package com.tpay.domains.point_scheduled.domain;


import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.PointStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point_scheduled")
@Entity
public class PointScheduledEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private long value;

    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    @ManyToOne
    @JoinColumn(name = "franchisee_id")
    private FranchiseeEntity franchiseeEntity;

    @Builder
    public PointScheduledEntity(long value, PointStatus pointStatus, OrderEntity orderEntity, FranchiseeEntity franchiseeEntity) {
        this.value = value;
        this.pointStatus = pointStatus;
        this.orderEntity = orderEntity;
        this.franchiseeEntity = franchiseeEntity;
    }

    public void updateStatusSave() {
        this.pointStatus = PointStatus.SAVE;
    }
    public void updateStatusScheduledCancel() {
        this.pointStatus = PointStatus.SCHEDULED_CANCEL;
    }
}
