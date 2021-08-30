package com.tpay.domains.franchisee_applicant.domain;

import com.tpay.domains.BaseTimeEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "franchisee_applicant")
@Entity
public class FranchiseeApplicantEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String storeNumber;
  private StoreStatus storeStatus;
  private String rejectReason;

  @Builder
  public FranchiseeApplicantEntity() {
    this.storeNumber = "";
    this.storeStatus = StoreStatus.WAIT;
    this.rejectReason = "";
  }

  public FranchiseeApplicantEntity accept(String storeNumber) {
    this.storeNumber = storeNumber;
    this.storeStatus = StoreStatus.ACCEPTED;
    this.rejectReason = "";
    return this;
  }

  public FranchiseeApplicantEntity reject(String rejectReason) {
    this.rejectReason = rejectReason;
    this.storeStatus = StoreStatus.REJECTED;
    return this;
  }
}
