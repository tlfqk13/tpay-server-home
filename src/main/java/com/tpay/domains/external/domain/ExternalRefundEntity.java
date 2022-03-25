package com.tpay.domains.external.domain;

import com.tpay.domains.refund.domain.RefundEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "external_refund")
@Entity
public class ExternalRefundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long franchiseeIndex;

    @NotNull
    private Long customerIndex;

    @NotNull
    private ExternalRefundStatus externalRefundStatus;

    @OneToOne
    @JoinColumn(name = "refund_id")
    private RefundEntity refundEntity;

    @Builder
    public ExternalRefundEntity(Long franchiseeIndex, Long customerIndex, ExternalRefundStatus externalRefundStatus) {
        this.franchiseeIndex = franchiseeIndex;
        this.customerIndex = customerIndex;
        this.externalRefundStatus = externalRefundStatus;
    }

    public ExternalRefundEntity changeStatus(ExternalRefundStatus externalRefundStatus) {
        this.externalRefundStatus = externalRefundStatus;
        return this;
    }

    public ExternalRefundEntity refundIndexRegister(RefundEntity refundEntity){
        this.refundEntity = refundEntity;
        return this;
    }
}
