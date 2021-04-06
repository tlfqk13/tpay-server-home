package com.tpay.domains.point.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point")
@Entity
@ToString
public class PointEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private SignType signType;

  private long change;

  @Enumerated(EnumType.STRING)
  private PointStatus pointStatus;

  private long balance;

  @ManyToOne
  @JoinColumn(name = "franchisee_id")
  private FranchiseeEntity franchiseeEntity;

  @Builder
  public PointEntity(
      SignType signType,
      long change,
      PointStatus pointStatus,
      long balance,
      FranchiseeEntity franchiseeEntity) {
    this.signType = signType;
    this.change = change;
    this.pointStatus = pointStatus;
    this.balance = balance;
    this.franchiseeEntity = franchiseeEntity;
  }
}