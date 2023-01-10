package com.tpay.domains.erp.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.UnknownException;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.DetailFranchiseeInfo;
import com.tpay.domains.franchisee_applicant.application.dto.FilterSelector;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDetailUpdateResponse;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDto;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import com.tpay.domains.franchisee_upload.application.FranchiseeBankFindService;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.tpay.domains.franchisee_applicant.application.dto.FilterSelector.BOTH;
import static com.tpay.domains.franchisee_applicant.application.dto.FilterSelector.IS_READ;
import static com.tpay.domains.refund_core.application.dto.RefundCustomValue.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FranchiseeApplicantTestFindService {

    private final FranchiseeApplicantRepository franchiseeApplicantRepository;
    private final FranchiseeBankFindService franchiseeBankFindService;
    private final FranchiseeUploadFindService franchiseeUploadFindService;
    private final FranchiseeUploadService franchiseeUploadService;

    public FranchiseeApplicantEntity findByIndex(Long franchiseeApplicantIndex) {
        FranchiseeApplicantEntity franchiseeApplicantEntity =
                franchiseeApplicantRepository
                        .findById(franchiseeApplicantIndex)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Applicant Index"));

        return franchiseeApplicantEntity;
    }

    public FranchiseeApplicantEntity findByFranchiseeEntity(FranchiseeEntity franchiseeEntity) {
        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantRepository.findByFranchiseeEntity(franchiseeEntity)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Entity"));
        return franchiseeApplicantEntity;
    }

    public Page<FranchiseeApplicantDto.Response> findAll(Pageable pageable, String searchKeyword) {
        Page<FranchiseeApplicantDto.Response> response;
        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);
        response = franchiseeApplicantRepository.findFranchiseeAll(pageable, searchKeyword, isBusinessNumber);
        return response;
    }

    public Page<FranchiseeApplicantDto.Response> applicantFilterTest(Pageable pageable, FilterSelector filterSelector, String value, String searchKeyword) {
        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);

        FranchiseeStatus franchiseeStatus;
        Page<FranchiseeApplicantDto.Response> response;

        if (filterSelector.equals(IS_READ)) {
            response = franchiseeApplicantRepository.findFranchiseeAllFilter(pageable, searchKeyword, false, isBusinessNumber);
        } else if (filterSelector.equals(BOTH)) {
            franchiseeStatus = FranchiseeStatus.valueOf(value);
            response = franchiseeApplicantRepository.findFranchiseeAllFilter(pageable, searchKeyword, franchiseeStatus, false, isBusinessNumber);
        } else {
            franchiseeStatus = FranchiseeStatus.valueOf(value);
            response = franchiseeApplicantRepository.findFranchiseeAllFilter(pageable, searchKeyword, franchiseeStatus, true, isBusinessNumber);
        }

        return response;
    }

    @Transactional
    public FranchiseeApplicantDetailUpdateResponse updateFranchiseeApplicantInfo(Long franchiseeApplicantIndex, String imageCategory
            , String detailFranchiseeInfo, MultipartFile uploadImage, String isNewUploadedImg) {

        FranchiseeApplicantEntity franchiseeApplicantEntity = this.findByIndex(franchiseeApplicantIndex);
        FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();
        FranchiseeBankEntity franchiseeBankEntity;
        FranchiseeUploadEntity franchiseeUploadEntity;
        String taxFreeStoreNumberUpdate = null;
        FranchiseeEntity franchiseeEntityUpdate = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            DetailFranchiseeInfo request = objectMapper.readValue(detailFranchiseeInfo, DetailFranchiseeInfo.class);
            franchiseeEntityUpdate = franchiseeEntity.updateFranchisee(request);
            franchiseeBankEntity = franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);
            franchiseeBankEntity = franchiseeBankEntity.updateBankInfoFromAdmin(request);
            franchiseeUploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeEntity.getId());
            if (!request.getTaxFreeStoreNumber().isEmpty()) {
                taxFreeStoreNumberUpdate = franchiseeUploadEntity.updateTaxFreeStoreNumber(request.getTaxFreeStoreNumber());
            }
            if (isNewUploadedImg.equals("true") || isNewUploadedImg.equals("TRUE")) {
                String s3path = franchiseeUploadService.uploadImageAndBankInfo(franchiseeEntity.getId(), imageCategory, uploadImage);
            }
            if (REFUND_STEP_ONE.equals(request.getRefundStep())) {
                franchiseeEntity.updateRefundStep(REFUND_STEP_ONE);
            } else if (REFUND_STEP_TWO.equals(request.getRefundStep())) {
                franchiseeEntity.updateRefundStep(REFUND_STEP_TWO);
            } else {
                franchiseeEntity.updateRefundStep(REFUND_STEP_THREE);
            }
        } catch (InvalidParameterException e) {
            franchiseeBankEntity = FranchiseeBankEntity.builder().build();
        } catch (Exception e) {
            throw new UnknownException(ExceptionState.UNKNOWN, "detailFranchiseeInfo data parsing error");
        }

        return new FranchiseeApplicantDetailUpdateResponse(franchiseeEntityUpdate, franchiseeBankEntity, taxFreeStoreNumberUpdate);
    }
}