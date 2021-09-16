package com.tpay.domains.franchisee_applicant.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "franchisee_applicant")
@Entity
public class FranchiseeApplicantEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private FranchiseeStatus franchiseeStatus;
  private String rejectReason;

  @OneToOne
  @JoinColumn(name = "franchisee_id")
  private FranchiseeEntity franchiseeEntity;

  @Builder
  public FranchiseeApplicantEntity(FranchiseeEntity franchiseeEntity) {
    this.franchiseeStatus = FranchiseeStatus.WAIT;
    this.rejectReason = "";
    this.franchiseeEntity = franchiseeEntity;
  }

  public FranchiseeApplicantEntity accept() {
    this.franchiseeStatus = FranchiseeStatus.ACCEPTED;
    this.rejectReason = "";
    return this;
  }

  public FranchiseeApplicantEntity reject(String rejectReason) {
    this.rejectReason = rejectReason;
    this.franchiseeStatus = FranchiseeStatus.REJECTED;
    return this;
  }

  public FranchiseeApplicantEntity reapply() {
    this.franchiseeStatus = FranchiseeStatus.REAPPLIED;
    return this;
  }
}
