package com.tpay.domains.franchisee_upload.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FranchiseeUploadRepository extends JpaRepository<FranchiseeUploadEntity, Long> {

    boolean existsByFranchiseeIndexAndImageCategory(Long franchiseeIndex, String imageCategory);

    FranchiseeUploadEntity findByFranchiseeIndexAndImageCategory(Long franchiseeIndex, String imageCategory);

    Optional<FranchiseeUploadEntity> findByFranchiseeIndex(Long franchiseeIndex);

    void deleteByFranchiseeEntity(FranchiseeEntity franchiseeEntity);
}
