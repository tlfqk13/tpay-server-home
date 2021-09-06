package com.tpay.domains.franchisee_applicant.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FranchiseeApplicantFindService {

  private final FranchiseeApplicantRepository franchiseeApplicantRepository;

  public FranchiseeApplicantEntity findByIndex(Long franchiseeApplicantIndex) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        franchiseeApplicantRepository
            .findById(franchiseeApplicantIndex)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Applicant Index"));

    return franchiseeApplicantEntity;
  }

  public List<FranchiseeApplicantInfo> findAll() {
    List<FranchiseeApplicantInfo> franchiseeApplicantInfoList =
        franchiseeApplicantRepository.findAll().stream()
            .map(franchiseeApplicantEntity -> toResponse(franchiseeApplicantEntity))
            .collect(Collectors.toList());
    return franchiseeApplicantInfoList;
  }

  public FranchiseeApplicantInfo find(Long franchiseeApplicantIndex) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        this.findByIndex(franchiseeApplicantIndex);

    return toResponse(franchiseeApplicantEntity);
  }

  private FranchiseeApplicantInfo toResponse(
      FranchiseeApplicantEntity franchiseeApplicantEntity) {
    FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();

    return FranchiseeApplicantInfo.builder()
        .franchiseeApplicantIndex(franchiseeApplicantEntity.getId())
        .franchiseeStatus(franchiseeApplicantEntity.getFranchiseeStatus())
        .rejectReason(franchiseeApplicantEntity.getRejectReason())
        .memberName(franchiseeEntity.getMemberName())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .storeName(franchiseeEntity.getStoreName())
        .storeAddress(franchiseeEntity.getStoreAddress())
        .sellerName(franchiseeEntity.getSellerName())
        .storeTel(franchiseeEntity.getStoreTel())
        .productCategory(franchiseeEntity.getProductCategory())
        .build();
  }
}
