package com.tpay.domains.franchisee_applicant.domain;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfoInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FranchiseeApplicantRepository
    extends JpaRepository<FranchiseeApplicantEntity, Long> {
  Optional<FranchiseeApplicantEntity> findByFranchiseeEntityBusinessNumber(String businessNumber);

  Optional<FranchiseeApplicantEntity> findByFranchiseeEntity(FranchiseeEntity franchiseeEntity);

  @Query(value = "select fa.id                as franchiseeApplicantIndex,\n" +
      "       fa.franchisee_status as franchiseeStatus,\n" +
      "       f.mem_nm             as memberName,\n" +
      "       f.biz_no             as businessNumber,\n" +
      "       f.store_nm           as storeName,\n" +
      "       f.sel_nm             as sellerName,\n" +
      "       f.created_date       as createdDate,\n" +
      "       f.is_refund_once     as isRefundOnce,\n" +
      "       fa.is_read     as isRead\n" +
      "    from franchisee_applicant fa\n" +
      "        inner join franchisee f on fa.franchisee_id = f.id", nativeQuery = true)
  List<FranchiseeApplicantInfoInterface> findAllNativeQuery();
}
