package com.tpay.domains.franchisee_upload.application.dto;


import com.tpay.domains.franchisee.application.dto.FranchiseeInfo;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeBankInfo {
  private String accountNumber;
  private String bankName;
  private String withdrawalDate;



  public static FranchiseeBankInfo of(FranchiseeBankEntity franchiseeBankEntity) {
    return FranchiseeBankInfo.builder()
        .accountNumber(franchiseeBankEntity.getAccountNumber())
        .bankName(franchiseeBankEntity.getBankName())
        .withdrawalDate(franchiseeBankEntity.getWithdrawalDate())
        .build();
  }
}
