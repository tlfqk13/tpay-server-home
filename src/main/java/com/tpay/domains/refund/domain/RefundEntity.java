package com.tpay.domains.refund.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_after_id")
    private RefundAfterEntity refundAfterEntity;

    @Builder
    public RefundEntity(String responseCode, String orderNumber, String takeOutNumber, OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
        this.totalRefund = orderEntity.getTotalRefund();
        this.refundStatus = responseCode.equals("0000") ? RefundStatus.APPROVAL : RefundStatus.REJECT;
        this.takeOutNumber = takeOutNumber;

        orderEntity.setOrderNumber(orderNumber);
        orderEntity.setRefundEntity(this);
    }

    /**
     * 사후 환급 시, 사용되는 RefundEntity
     */
    @Builder
    public RefundEntity(String responseCode, String takeOutNumber, OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
        this.totalRefund = orderEntity.getTotalRefund();
        this.takeOutNumber = takeOutNumber;
        this.refundStatus = getResponseCode(responseCode, takeOutNumber);

        orderEntity.setRefundEntity(this);
    }

    public void updateCancel(String responseCode) {
        if (responseCode.equals("0000")) {
            this.refundStatus = RefundStatus.CANCEL;
        }
    }

    public void addRefundAfterEntity(RefundAfterEntity refundAfterEntity) {
        this.refundAfterEntity = refundAfterEntity;
    }

    private RefundStatus getResponseCode(String responseCode, String takeOutNumber) {
        List<String> preApprovalList = List.of("1005", "1010");
        if (takeOutNumber.contains("acpt") || preApprovalList.contains(responseCode)) {
            return RefundStatus.PRE_APPROVAL;
        }

        if (responseCode.equals("0000")) {
            return RefundStatus.APPROVAL;
        }

        return RefundStatus.REJECT;
    }

    public void updateTakeOutInfo(String takeOutNumber) {
        this.takeOutNumber = takeOutNumber;
        this.refundStatus = RefundStatus.APPROVAL;
        // VAN 에서 요청들어오면 그 시간으로 넣어줘야함
        this.refundAfterEntity.updateApprovalFinishDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
    }
}
