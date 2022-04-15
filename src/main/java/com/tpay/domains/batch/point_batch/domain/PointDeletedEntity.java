package com.tpay.domains.batch.point_batch.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import com.tpay.domains.point.domain.PointEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point_deleted")
@Entity
@ToString
public class PointDeletedEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long change;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    @ManyToOne
    @JoinColumn(name = "franchisee_id")
    private FranchiseeEntity franchiseeEntity;

    public PointDeletedEntity(PointEntity pointEntity) {
        this.change = pointEntity.getChange();
        this.orderEntity = pointEntity.getOrderEntity();
        this.franchiseeEntity = pointEntity.getFranchiseeEntity();
    }
}
