package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidBusinessNumberException;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.regex.RegExType;
import com.tpay.commons.regex.RegExUtils;
import com.tpay.domains.franchisee.application.dto.FranchiseeUpdateInfo;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeUpdateService {

  private final FranchiseeFindService franchiseeFindService;
  private final RegExUtils regExUtils;

  @Transactional
  public FranchiseeUpdateInfo update(Long franchiseeIndex, FranchiseeUpdateInfo request) {
    FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);

    // 수정 안되어도 Request 전체 다 옴 (근데 왜 patch?)
    if (!regExUtils.validate(RegExType.EMAIL, request.getEmail())){
      throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER,"Invalid Email Format");
    }

    franchiseeEntity.modifyInfo(
        request.getStoreNumber(),
        request.getEmail());

    return FranchiseeUpdateInfo.builder()
        .storeNumber(franchiseeEntity.getStoreNumber())
        .email(franchiseeEntity.getEmail())
        .build();
  }
}
