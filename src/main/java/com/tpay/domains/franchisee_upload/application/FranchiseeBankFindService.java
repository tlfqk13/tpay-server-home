package com.tpay.domains.franchisee_upload.application;


import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FranchiseeBankFindService {

  private final FranchiseeBankRepository franchiseeBankRepository;

  public FranchiseeBankEntity findByFranchiseeEntity(FranchiseeEntity franchiseeEntity){
    Optional<FranchiseeBankEntity> optionalFranchiseeBankEntity = franchiseeBankRepository.findByFranchiseeEntity(franchiseeEntity);
    if(optionalFranchiseeBankEntity.isEmpty()){
      return FranchiseeBankEntity.builder().build();
    }
    return optionalFranchiseeBankEntity.get();
  }
}
