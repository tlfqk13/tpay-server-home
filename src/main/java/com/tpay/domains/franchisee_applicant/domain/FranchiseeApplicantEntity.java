package com.tpay.domains.franchisee_applicant.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

  @Column(length = 10)
  private Boolean isRead;

  @OneToOne
  @JoinColumn(name = "franchisee_id")
  private FranchiseeEntity franchiseeEntity;

  @Builder
  public FranchiseeApplicantEntity(FranchiseeEntity franchiseeEntity) {
    this.franchiseeStatus = FranchiseeStatus.INIT;
    this.rejectReason = "";
    this.isRead = false;
    this.franchiseeEntity = franchiseeEntity;
  }

  public FranchiseeApplicantEntity accept() {
    this.franchiseeStatus = FranchiseeStatus.ACCEPTED;
    this.rejectReason = "";
    return this;
  }

  public void apply() {
    this.franchiseeStatus = FranchiseeStatus.WAIT;
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

  public void read(){
    this.isRead = !isRead;
  }

}
