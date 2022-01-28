package com.tpay.domains.franchisee_upload.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
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
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "Franchisee Bank Entity doesn't exists");
    }
    return optionalFranchiseeBankEntity.get();
  }
}
