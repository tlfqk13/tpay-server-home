package com.tpay.domains.refund.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.sale.domain.SaleEntity;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
  private String approvalNumber;

  @Column(name = "totRefund", length = 10)
  private String totalRefund;

  @Enumerated(EnumType.STRING)
  private RefundStatus refundStatus;

  @OneToOne
  @JoinColumn(name = "sale_id")
  private SaleEntity saleEntity;

  @Builder
  public RefundEntity(
      String approvalNumber, String totalRefund, RefundStatus refundStatus, SaleEntity saleEntity) {
    this.approvalNumber = approvalNumber;
    this.totalRefund = totalRefund;
    this.refundStatus = refundStatus;
    this.saleEntity = saleEntity;
  }

  public void updateCancel(String responseCode) {
    if (responseCode.equals("0000")) {
      this.refundStatus = RefundStatus.CANCEL;
    }
  }
}
