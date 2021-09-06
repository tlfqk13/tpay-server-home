package com.tpay.domains.franchisee_applicant.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseeApplicantRepository
    extends JpaRepository<FranchiseeApplicantEntity, Long> {
  Optional<FranchiseeApplicantEntity> findByFranchiseeEntityBusinessNumber(String businessNumber);
}
