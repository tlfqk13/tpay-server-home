package com.tpay.domains.refund.domain;

import com.tpay.domains.van.domain.PaymentEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "refundafter")
public class RefundAfterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_after_id")
    private Long id;

    private String cusCode;
    private String localCode;
    private String kioskBsnmCode;
    private String kioskCode;
    private String cityRefundCenterCode;

    @Enumerated(EnumType.STRING)
    private RefundAfterMethod refundAfterMethod;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    public void addPayment(PaymentEntity payment) {
        this.payment = payment;
    }
}
