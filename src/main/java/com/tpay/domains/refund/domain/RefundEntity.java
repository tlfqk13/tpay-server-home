package com.tpay.domains.refund.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refund")
@Entity
@ToString
public class RefundEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tkOutConfNo", length = 20)
    private String takeOutNumber;

    @Column(name = "totRefund", length = 10)
    private String totalRefund;

    //  @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    @Builder
    public RefundEntity(String responseCode, String orderNumber, String takeOutNumber, OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
        this.totalRefund = orderEntity.getTotalRefund();
        this.refundStatus = responseCode.equals("0000") ? RefundStatus.APPROVAL : RefundStatus.REJECT;
        this.takeOutNumber = takeOutNumber;

        orderEntity.setOrderNumber(orderNumber);
        orderEntity.setRefundEntity(this);
    }

    public void updateCancel(String responseCode) {
        if (responseCode.equals("0000")) {
            this.refundStatus = RefundStatus.CANCEL;
        }
    }
}
