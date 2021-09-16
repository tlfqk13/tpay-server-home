package com.tpay.domains.franchisee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidBusinessNumberException;
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

    String businessNumber = request.getBusinessNumber();
    if (!regExUtils.validate(RegExType.BUSINESS_NUMBER, businessNumber)) {
      throw new InvalidBusinessNumberException(
          ExceptionState.INVALID_BUSINESS_NUMBER,
          "Required Business Number Format : (XXX-XX-XXXXX)");
    }

    franchiseeEntity.modifyInfo(
        request.getStoreName(),
        request.getStoreAddress(),
        request.getBusinessNumber(),
        request.getProductCategory());

    return FranchiseeUpdateInfo.builder()
        .storeName(franchiseeEntity.getStoreName())
        .storeAddress(franchiseeEntity.getStoreAddress())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .productCategory(franchiseeEntity.getProductCategory())
        .build();
  }
}
