package com.tpay.domains.franchisee_upload.application;


import com.tpay.domains.franchisee_upload.application.dto.FranchiseeBankInfo;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeBankService {

  private final FranchiseeBankRepository franchiseeBankRepository;

  public FranchiseeBankInfo save(FranchiseeBankInfo franchiseeBankInfo) {
    FranchiseeBankEntity franchiseeBankEntity = FranchiseeBankEntity.builder()
        .accountNumber(franchiseeBankInfo.getAccountNumber())
        .bankName(franchiseeBankInfo.getBankName())
        .withdrawalDate(franchiseeBankInfo.getWithdrawalDate())
        .build();
    // TODO: 2022/01/12 은행컬럼들의 각 필요이유에 따라 설계 수정될 가능성 있음
    FranchiseeBankEntity result = franchiseeBankRepository.save(franchiseeBankEntity);
    return FranchiseeBankInfo.of(result);
  }
}