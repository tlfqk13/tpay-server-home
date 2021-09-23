package com.tpay.domains.refund.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.order.domain.OrderEntity;

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
  @JoinColumn(name = "order_id")
  private OrderEntity orderEntity;

  @Builder
  public RefundEntity(
      String approvalNumber, String totalRefund, RefundStatus refundStatus, OrderEntity orderEntity) {
    this.approvalNumber = approvalNumber;
    this.totalRefund = totalRefund;
    this.refundStatus = refundStatus;
    this.orderEntity = orderEntity;
  }

  public void updateCancel(String responseCode) {
    if (responseCode.equals("0000")) {
      this.refundStatus = RefundStatus.CANCEL;
    }
  }
}
