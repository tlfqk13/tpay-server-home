package com.tpay.domains.franchisee_upload.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface BusinessLicenseRepository extends JpaRepository<BusinessLicenseEntity, Long> {

}
