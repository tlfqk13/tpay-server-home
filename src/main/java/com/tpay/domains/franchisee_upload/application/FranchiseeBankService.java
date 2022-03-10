package com.tpay.domains.franchisee_upload.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.application.dto.FranchiseeBankInfo;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FranchiseeBankService {

  private final FranchiseeBankRepository franchiseeBankRepository;
  private final FranchiseeFindService franchiseeFindService;
  private final FranchiseeBankFindService franchiseeBankFindService;

  public FranchiseeBankInfo save(Long franchiseeIndex, FranchiseeBankInfo franchiseeBankInfo) {

    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
    FranchiseeBankEntity franchiseeBankEntity = FranchiseeBankEntity.builder()
        .accountNumber(franchiseeBankInfo.getAccountNumber().replaceAll("-", ""))
        .bankName(franchiseeBankInfo.getBankName())
        .withdrawalDate(franchiseeBankInfo.getWithdrawalDate())
        .franchiseeEntity(franchiseeEntity)
        .build();
    FranchiseeBankEntity result = franchiseeBankRepository.save(franchiseeBankEntity);
    return FranchiseeBankInfo.of(result);
  }

  public FranchiseeBankInfo findMyAccount(Long franchiseeIndex) {
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
    FranchiseeBankEntity franchiseeBankEntity = franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);
    return FranchiseeBankInfo.of(franchiseeBankEntity,franchiseeEntity);
  }

  @Transactional
  public FranchiseeBankInfo updateMyAccount(Long franchiseeIndex, FranchiseeBankInfo franchiseeBankInfo) {
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
    FranchiseeBankEntity franchiseeBankEntity = franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);
    if(franchiseeBankEntity.getBankName().isEmpty()){
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER,"Bank account must exist before UPDATE");
    }
    FranchiseeBankEntity franchiseeBankEntity1 = franchiseeBankEntity.updateBankInfo(franchiseeBankInfo);
    return FranchiseeBankInfo.of(franchiseeBankEntity1);
  }
}
