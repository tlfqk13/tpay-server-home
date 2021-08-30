package com.tpay.domains.franchisee_applicant.application;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantFindResponse;
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

  public List<FranchiseeApplicantFindResponse> findAll() {
    List<FranchiseeApplicantFindResponse> franchiseeApplicantFindResponseList =
        franchiseeApplicantRepository.findAll().stream()
            .map(franchiseeApplicantEntity -> toResponse(franchiseeApplicantEntity))
            .collect(Collectors.toList());
    return franchiseeApplicantFindResponseList;
  }

  public FranchiseeApplicantFindResponse find(Long franchiseeApplicantIndex) {
    FranchiseeApplicantEntity franchiseeApplicantEntity =
        this.findByIndex(franchiseeApplicantIndex);

    return toResponse(franchiseeApplicantEntity);
  }

  private FranchiseeApplicantFindResponse toResponse(
      FranchiseeApplicantEntity franchiseeApplicantEntity) {
    FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();

    return FranchiseeApplicantFindResponse.builder()
        .franchiseeApplicantIndex(franchiseeApplicantEntity.getId())
        .storeStatus(franchiseeApplicantEntity.getStoreStatus())
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
