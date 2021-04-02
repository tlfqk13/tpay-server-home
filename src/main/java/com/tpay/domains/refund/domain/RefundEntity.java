package com.tpay.domains.refund.domain;

import com.tpay.domains.sale.domain.SaleEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refund")
@Entity
@ToString
public class RefundEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tkOutConfNo", length = 20)
  private String approvalNumber;

  @Column(name = "totRefund", length = 10)
  private String totalRefund;

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
}
