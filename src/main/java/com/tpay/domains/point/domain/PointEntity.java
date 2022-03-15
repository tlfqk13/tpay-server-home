package com.tpay.domains.point.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "points")
@Entity
@ToString
public class PointEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime createdDate;

  @Enumerated(EnumType.STRING)
  private SignType signType;

  @Column(name = "change_value")
  private long change;

  @Enumerated(EnumType.STRING)
  private PointStatus pointStatus;

  private long balance;

  private long withdrawalCheck;

  @Column(length = 1)
  private Boolean isRead;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private OrderEntity orderEntity;

  @ManyToOne
  @JoinColumn(name = "franchisee_id")
  private FranchiseeEntity franchiseeEntity;

  @Builder
  public PointEntity(
      LocalDateTime createdDate,
      SignType signType,
      long change,
      PointStatus pointStatus,
      long balance,
      OrderEntity orderEntity,
      FranchiseeEntity franchiseeEntity) {
    this.createdDate = createdDate;
    this.signType = signType;
    this.change = change;
    this.pointStatus = pointStatus;
    this.balance = balance;
    this.orderEntity = orderEntity;
    this.franchiseeEntity = franchiseeEntity;
    this.withdrawalCheck = change;
    this.isRead = false;
  }

  public void updateStatus() {
    this.pointStatus = PointStatus.SAVE;
  }

  public void updateWithdrawalCheck(Long amount) {
    this.withdrawalCheck -= amount;
  }
}
