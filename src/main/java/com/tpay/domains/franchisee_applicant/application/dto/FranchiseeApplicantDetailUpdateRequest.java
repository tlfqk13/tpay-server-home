package com.tpay.domains.franchisee_applicant.application.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class FranchiseeApplicantDetailUpdateRequest {
    // Franchisee
    DetailFranchiseeInfo detailFranchiseeInfo;

    private boolean isNewUploadedImg;

    MultipartFile uploadImage;
}
