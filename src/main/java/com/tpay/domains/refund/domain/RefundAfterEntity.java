package com.tpay.domains.refund.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class RefundAfterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_after_id")
    private Long id;

    private String localCode;
    private String kioskBsnmCode;
    private String kioskCode;
    private String cityRefundCenterCode;

    @Enumerated(EnumType.STRING)
    private RefundAfterMethod refundAfterMethod;
}
