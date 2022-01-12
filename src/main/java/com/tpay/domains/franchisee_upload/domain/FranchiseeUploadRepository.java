package com.tpay.domains.franchisee_upload.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FranchiseeUploadRepository extends JpaRepository<FranchiseeUploadEntity, Long> {

  boolean existsByFranchiseeIndexAndImageCategory(String franchiseeIndex, String imageCategory);
  FranchiseeUploadEntity findByFranchiseeIndexAndImageCategory(String franchiseeIndex, String imageCategory);
}
