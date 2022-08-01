package com.tpay.domains.franchisee_applicant.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.search.application.dto.SearchListInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    Page<FranchiseeApplicantEntity> findAllByOrderByIdDesc(Pageable pageable);
    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<FranchiseeApplicantEntity> findByFranchiseeEntityBusinessNumber(Pageable pageable,@Param("businessNumber") String searchBusinessNumber);
    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<FranchiseeApplicantEntity> findByFranchiseeEntityStoreName(Pageable pageable,@Param("storeName") String searchStoreName);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    List<FranchiseeApplicantEntity> findByFranchiseeStatus(FranchiseeStatus franchiseeStatus);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    List<FranchiseeApplicantEntity> findByIsReadInAndFranchiseeStatusInOrderByIdDesc(@Param("isRead") List<Boolean> isRead, @Param("franchiseeStatus") List<FranchiseeStatus> franchiseeStatus);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    List<FranchiseeApplicantEntity> findByIsReadOrderByIdDesc(@Param("isRead") Boolean isRead);

    @Query(value = "select\n" +
            " f.store_nm as storeName,\n" +
            " f.biz_no as businessNumber\n" +
            " from franchisee f\n" +
            " order by f.id  desc", nativeQuery = true)
    List<SearchListInterface> findSearchListFranchisee();

}