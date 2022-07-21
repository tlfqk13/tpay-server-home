package com.tpay.domains.franchisee_applicant.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FranchiseeApplicantRepository
    extends JpaRepository<FranchiseeApplicantEntity, Long> {
    Optional<FranchiseeApplicantEntity> findByFranchiseeEntityBusinessNumber(String businessNumber);

    Optional<FranchiseeApplicantEntity> findByFranchiseeEntity(FranchiseeEntity franchiseeEntity);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    List<FranchiseeApplicantEntity> findAllByOrderByIdDesc();

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    List<FranchiseeApplicantEntity> findAllByOrderByIdDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    List<FranchiseeApplicantEntity> findByFranchiseeStatus(FranchiseeStatus franchiseeStatus);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    List<FranchiseeApplicantEntity> findByIsReadInAndFranchiseeStatusInOrderByIdDesc(@Param("isRead") List<Boolean> isRead, @Param("franchiseeStatus") List<FranchiseeStatus> franchiseeStatus);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    List<FranchiseeApplicantEntity> findByIsReadOrderByIdDesc(@Param("isRead") Boolean isRead);

}