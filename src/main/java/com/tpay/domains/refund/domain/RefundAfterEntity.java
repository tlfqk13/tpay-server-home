package com.tpay.domains.refund.domain;

import com.tpay.domains.van.domain.PaymentEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "refund_after")
public class RefundAfterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_after_id")
    private Long id;

    private String cusCode;
    private String locaCode;
    private String kioskBsnmCode;
    private String kioskCode;
    private String cityRefundCenterCode;
    private String approvalFinishDate;

    @Enumerated(EnumType.STRING)
    private RefundAfterMethod refundAfterMethod;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    public void addPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public void updateApprovalFinishDate(String approvalFinishDate) {
        this.approvalFinishDate = approvalFinishDate;
    }
}
