package com.tpay.domains.franchisee_applicant.application;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.DetailFranchiseeInfo;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDetailUpdateResponse;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import com.tpay.domains.franchisee_upload.application.FranchiseeBankFindService;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadFindService;
import com.tpay.domains.franchisee_upload.application.FranchiseeUploadService;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.tpay.domains.refund_core.application.dto.RefundCustomValue.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FranchiseeApplicantUpdateService {

    private final FranchiseeBankFindService franchiseeBankFindService;
    private final FranchiseeUploadFindService franchiseeUploadFindService;
    private final FranchiseeUploadService franchiseeUploadService;
    private final FranchiseeFindService franchiseeFindService;

    private final FranchiseeApplicantRepository franchiseeApplicantRepository;

    @Transactional
    public FranchiseeApplicantDetailUpdateResponse updateFranchiseeApplicantInfo(Long franchiseeApplicantIndex, String imageCategory
            , String detailFranchiseeInfo, MultipartFile uploadImage, String isNewUploadedImg) {

        FranchiseeApplicantEntity franchiseeApplicantEntity = this.findByIndex(franchiseeApplicantIndex);
        FranchiseeEntity franchiseeEntity = franchiseeApplicantEntity.getFranchiseeEntity();
        FranchiseeBankEntity franchiseeBankEntity = null;
        FranchiseeUploadEntity franchiseeUploadEntity;
        String taxFreeStoreNumberUpdate = null;
        FranchiseeEntity franchiseeEntityUpdate = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

            DetailFranchiseeInfo request = objectMapper.readValue(detailFranchiseeInfo, DetailFranchiseeInfo.class);
            franchiseeEntityUpdate = franchiseeEntity.updateFranchisee(request);

            franchiseeBankEntity = franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);
            franchiseeBankEntity = franchiseeBankEntity.updateBankInfoFromAdmin(request);
            franchiseeUploadEntity = franchiseeUploadFindService.findByFranchiseeIndex(franchiseeEntity.getId());
            if(!request.getTaxFreeStoreNumber().isEmpty()){
                taxFreeStoreNumberUpdate = franchiseeUploadEntity.updateTaxFreeStoreNumber(request.getTaxFreeStoreNumber());
            }
            if (isNewUploadedImg.equals("true") || isNewUploadedImg.equals("TRUE")) {
                String s3path = franchiseeUploadService.uploadImageAndBankInfo(franchiseeEntity.getId(), imageCategory, uploadImage);
            }
            if (REFUND_STEP_ONE.equals(request.getRefundStep())) {
                franchiseeEntity.updateRefundStep(REFUND_STEP_ONE);
            } else if(REFUND_STEP_TWO.equals(request.getRefundStep())) {
                franchiseeEntity.updateRefundStep(REFUND_STEP_TWO);
            }else {
                franchiseeEntity.updateRefundStep(REFUND_STEP_THREE);
            }
        } catch (InvalidParameterException e) {
            franchiseeBankEntity = FranchiseeBankEntity.builder().build();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return new FranchiseeApplicantDetailUpdateResponse(franchiseeEntityUpdate, franchiseeBankEntity, taxFreeStoreNumberUpdate);
    }

    private FranchiseeApplicantEntity findByIndex(Long franchiseeApplicantIndex) {
        FranchiseeApplicantEntity franchiseeApplicantEntity =
                franchiseeApplicantRepository
                        .findById(franchiseeApplicantIndex)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Franchisee Applicant Index"));

        return franchiseeApplicantEntity;
    }
}
