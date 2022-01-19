package com.tpay.domains.franchisee_upload.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FranchiseeUploadRepository extends JpaRepository<FranchiseeUploadEntity, Long> {

  boolean existsByFranchiseeIndexAndImageCategory(Long franchiseeIndex, String imageCategory);
  FranchiseeUploadEntity findByFranchiseeIndexAndImageCategory(Long franchiseeIndex, String imageCategory);
}
