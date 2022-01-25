package com.tpay.domains.franchisee_upload.domain;


import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.dto.FranchiseeBankInfo;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Getter
@Table(name = "franchisee_bank")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Entity
public class FranchiseeBankEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String accountNumber;

  @NotNull
  private String bankName;

  @NotNull
  private String withdrawalDate;

  @OneToOne
  @JoinColumn(name = "franchisee_id")
  private FranchiseeEntity franchiseeEntity;

  public FranchiseeBankEntity updateBankInfo(FranchiseeBankInfo franchiseeBankInfo) {
    this.accountNumber = franchiseeBankInfo.getAccountNumber();
    this.bankName = franchiseeBankInfo.getBankName();
    this.withdrawalDate = franchiseeBankInfo.getWithdrawalDate().replaceAll("일","");
    return this;
  }
}


