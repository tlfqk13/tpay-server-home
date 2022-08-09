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
    Page<FranchiseeApplicantEntity> findByFranchiseeEntityBusinessNumberContaining(Pageable pageable,@Param("businessNumber") String searchBusinessNumber);
    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<FranchiseeApplicantEntity> findByFranchiseeEntityStoreNameContaining(Pageable pageable,@Param("storeName") String searchStoreName);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    List<FranchiseeApplicantEntity> findByFranchiseeStatus(FranchiseeStatus franchiseeStatus);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<FranchiseeApplicantEntity> findByIsReadInAndFranchiseeStatusInOrderByIdDesc(
            @Param("isRead") List<Boolean> isRead, @Param("franchiseeStatus") List<FranchiseeStatus> franchiseeStatus,Pageable pageable);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<FranchiseeApplicantEntity> findByIsReadInAndFranchiseeStatusInAndFranchiseeEntityBusinessNumberContainingOrderByIdDesc(
            @Param("isRead") List<Boolean> isRead, @Param("franchiseeStatus") List<FranchiseeStatus> franchiseeStatus
            ,Pageable pageable,@Param("businessNumber") String businessNumber);

    @Query(value = "select f.id as id,\n"
            + " fa.franchisee_status as franchiseeStatus,\n"
            + " fa.is_read as isRead,\n"
            + " f.created_date as createdDate,\n"
            + " f.modified_date as modifiedDate,\n"
            + " f.biz_no as businessNumber,\n"
            + " f.sel_nm as sellerName,\n "
            + " f.store_nm as storeName\n"
            + " from franchisee_applicant fa\n"
            + " left outer join franchisee f on fa.franchisee_id = f.id\n"
            + " where f.biz_no like %:businessNumber%\n"
            + " and fa.is_read in :isRead\n"
            + " and fa.franchisee_status in :franchiseeStatus\n"
            + " order by f.id desc\n",
          countQuery = "select\n"
                  + " f.id as id,\n"
                  + " fa.franchisee_status as franchiseeStatus,\n"
                  + " fa.is_read as isRead,\n"
                  + " f.created_date as createdDate,\n"
                  + " f.modified_date as modifiedDate,\n"
                  + " f.biz_no as businessNumber,\n"
                  + " f.sel_nm as sellerName,\n "
                  + " f.store_nm as storeName\n"
                  + " from franchisee_applicant fa\n"
                  + " left outer join franchisee f on fa.franchisee_id = f.id\n"
                  + " where f.biz_no like %:businessNumber%\n"
                  + " and fa.is_read in :isRead\n"
                  + " and fa.franchisee_status in :franchiseeStatus\n"
                  + " order by f.id desc\n", nativeQuery = true)
    Page<FranchiseeApplicantEntity> filterAndBusinessNumber(
            @Param("isRead") List<Boolean> isRead, @Param("franchiseeStatus") List<FranchiseeStatus> franchiseeStatus
            ,Pageable pageable,@Param("businessNumber") String businessNumber);

    @Query(value = "select f.id as id,\n"
            + " fa.franchisee_status as franchiseeStatus,\n"
            + " fa.is_read as isRead,\n"
            + " f.created_date as createdDate,\n"
            + " f.modified_date as modifiedDate,\n"
            + " f.biz_no as businessNumber,\n"
            + " f.sel_nm as sellerName,\n "
            + " f.store_nm as storeName\n"
            + " from franchisee_applicant fa\n"
            + " left outer join franchisee f on fa.franchisee_id = f.id\n"
            + " where f.store_nm like %:storeName%\n"
            + " and fa.is_read in :isRead\n"
            + " and fa.franchisee_status in :franchiseeStatus\n"
            + " order by f.id desc\n",
            countQuery = "select\n"
                    + " f.id as id,\n"
                    + " fa.franchisee_status as franchiseeStatus,\n"
                    + " fa.is_read as isRead,\n"
                    + " f.created_date as createdDate,\n"
                    + " f.modified_date as modifiedDate,\n"
                    + " f.biz_no as businessNumber,\n"
                    + " f.sel_nm as sellerName,\n "
                    + " f.store_nm as storeName\n"
                    + " from franchisee_applicant fa\n"
                    + " left outer join franchisee f on fa.franchisee_id = f.id\n"
                    + " where f.store_nm like %:storeName%\n"
                    + " and fa.is_read in :isRead\n"
                    + " and fa.franchisee_status in :franchiseeStatus\n"
                    + " order by f.id desc\n", nativeQuery = true)
    Page<FranchiseeApplicantEntity> filterAndStoreName(
            @Param("isRead") List<Boolean> isRead, @Param("franchiseeStatus") List<FranchiseeStatus> franchiseeStatus
            ,Pageable pageable,@Param("storeName") String storeName);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<FranchiseeApplicantEntity> findByIsReadInAndFranchiseeStatusInAndFranchiseeEntityStoreNameContainingOrderByIdDesc(
            @Param("isRead") List<Boolean> isRead, @Param("franchiseeStatus") List<FranchiseeStatus> franchiseeStatus
            ,Pageable pageable,@Param("storeName") String storeName);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<FranchiseeApplicantEntity> findByIsReadOrderByIdDesc(@Param("isRead") Boolean isRead,Pageable pageable);


    @Query(value = "select f.id as id,\n"
            + " fa.franchisee_status as franchiseeStatus,\n"
            + " fa.is_read as isRead,\n"
            + " f.created_date as createdDate,\n"
            + " f.modified_date as modifiedDate,\n"
            + " f.biz_no as businessNumber,\n"
            + " f.sel_nm as sellerName,\n "
            + " f.store_nm as storeName\n"
            + " from franchisee_applicant fa\n"
            + " left outer join franchisee f on fa.franchisee_id = f.id\n"
            + " where f.biz_no like %:businessNumber%\n"
            + " and fa.is_read = :isRead\n"
            + " order by f.id desc\n",
            countQuery = "select\n"
                    + " f.id as id,\n"
                    + " fa.franchisee_status as franchiseeStatus,\n"
                    + " fa.is_read as isRead,\n"
                    + " f.created_date as createdDate,\n"
                    + " f.modified_date as modifiedDate,\n"
                    + " f.biz_no as businessNumber,\n"
                    + " f.sel_nm as sellerName,\n "
                    + " f.store_nm as storeName\n"
                    + " from franchisee_applicant fa\n"
                    + " left outer join franchisee f on fa.franchisee_id = f.id\n"
                    + " where f.biz_no like %:businessNumber%\n"
                    + " and fa.is_read = :isRead\n"
                    + " order by f.id desc\n", nativeQuery = true)
    Page<FranchiseeApplicantEntity> filterIsReadAndBusinessNumber(@Param("isRead") Boolean isRead
            ,Pageable pageable,@Param("businessNumber") String businessNumber);

    @Query(value = "select f.id as id,\n"
            + " fa.franchisee_status as franchiseeStatus,\n"
            + " fa.is_read as isRead,\n"
            + " f.created_date as createdDate,\n"
            + " f.modified_date as modifiedDate,\n"
            + " f.biz_no as businessNumber,\n"
            + " f.sel_nm as sellerName,\n "
            + " f.store_nm as storeName\n"
            + " from franchisee_applicant fa\n"
            + " left outer join franchisee f on fa.franchisee_id = f.id\n"
            + " where f.store_nm like %:storeName%\n"
            + " and fa.is_read = :isRead\n"
            + " order by f.id desc\n",
            countQuery = "select\n"
                    + " f.id as id,\n"
                    + " fa.franchisee_status as franchiseeStatus,\n"
                    + " fa.is_read as isRead,\n"
                    + " f.created_date as createdDate,\n"
                    + " f.modified_date as modifiedDate,\n"
                    + " f.biz_no as businessNumber,\n"
                    + " f.sel_nm as sellerName,\n "
                    + " f.store_nm as storeName\n"
                    + " from franchisee_applicant fa\n"
                    + " left outer join franchisee f on fa.franchisee_id = f.id\n"
                    + " where f.store_nm like %:storeName%\n"
                    + " and fa.is_read = :isRead\n"
                    + " order by f.id desc\n", nativeQuery = true)
    Page<FranchiseeApplicantEntity> filterIsReadAndStoreName(@Param("isRead") Boolean isRead
            ,Pageable pageable,@Param("storeName") String storeName);
    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<FranchiseeApplicantEntity> findByIsReadAndFranchiseeEntityBusinessNumberContainingOrderByIdDesc(@Param("isRead") Boolean isRead
            ,Pageable pageable,@Param("businessNumber") String businessNumber);

    @EntityGraph(attributePaths = {"franchiseeEntity"})
    Page<FranchiseeApplicantEntity> findByIsReadAndFranchiseeEntityStoreNameContainingOrderByIdDesc(@Param("isRead") Boolean isRead
            ,Pageable pageable,@Param("storeName") String storeName);

    @Query(value = "select\n" +
            " f.store_nm as storeName,\n" +
            " f.biz_no as businessNumber\n" +
            " from franchisee f\n" +
            " order by f.id  desc", nativeQuery = true)
    List<SearchListInterface> findSearchListFranchisee();

}