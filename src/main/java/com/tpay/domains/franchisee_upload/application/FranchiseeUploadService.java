package com.tpay.domains.franchisee_upload.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.aws.S3FileUploader;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.AlreadyExistsException;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.UnknownException;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.FranchiseeApplicantFindService;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_upload.application.dto.FranchiseeBankInfo;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeBankRepository;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadEntity;
import com.tpay.domains.franchisee_upload.domain.FranchiseeUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class FranchiseeUploadService {

    private final S3FileUploader s3FileUploader;
    private final FranchiseeUploadRepository franchiseeUploadRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final FranchiseeBankRepository franchiseeBankRepository;
    private final FranchiseeApplicantFindService franchiseeApplicantFindService;
    private final FranchiseeBankFindService franchiseeBankFindService;

    @Transactional
    public String uploadImageAndBankInfo(Long franchiseeIndex, String franchiseeBankInfoString, String imageCategory, MultipartFile uploadImage) {
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        boolean checkExistBank = franchiseeBankRepository.existsByFranchiseeEntity(franchiseeEntity);
        boolean checkUploadImage = franchiseeUploadRepository.existsByFranchiseeIndexAndImageCategory(franchiseeIndex, imageCategory);
        String s3Path;
        if (checkExistBank || checkUploadImage) {
            throw new AlreadyExistsException(ExceptionState.ALREADY_EXISTS, "This franchisee Already Exists [Bank Info] or [S3-Image]");
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FranchiseeBankInfo franchiseeBankInfo = objectMapper.readValue(franchiseeBankInfoString, FranchiseeBankInfo.class);
            // 은행 정보 저장 이슈 2022/12/22
            if (!franchiseeBankInfo.getBankName().isEmpty()) {
                FranchiseeBankEntity franchiseeBankEntity = FranchiseeBankEntity.builder()
                        .accountNumber(franchiseeBankInfo.getAccountNumber().replaceAll("-", ""))
                        .bankName(franchiseeBankInfo.getBankName())
                        .withdrawalDate(franchiseeBankInfo.getWithdrawalDate().replaceAll("일", ""))
                        .franchiseeEntity(franchiseeEntity)
                        .build();
                franchiseeBankRepository.save(franchiseeBankEntity);
                printNewFranchisee();
            } else {
                printNewFranchisee();
            }

            s3Path = s3FileUploader.uploadJpg(franchiseeIndex, imageCategory, uploadImage);
            FranchiseeUploadEntity franchiseeUploadEntity = FranchiseeUploadEntity.builder()
                    .franchiseeIndex(franchiseeIndex).imageCategory(imageCategory)
                    .taxFreeStoreNumber("")
                    .s3Path(s3Path)
                    .franchiseeEntity(franchiseeEntity)
                    .build();
            franchiseeUploadRepository.save(franchiseeUploadEntity);
        } catch (Exception e) {
            throw new UnknownException(ExceptionState.UNKNOWN, "[Bank Info] or [S3-Image] save fail");
        }


        FranchiseeApplicantEntity franchiseeApplicantEntity = franchiseeApplicantFindService.findByFranchiseeEntity(franchiseeEntity);
        franchiseeApplicantEntity.apply();
        return s3Path;

    }

    @Transactional
    public String uploadUpdateImageAndBankInfo(Long franchiseeIndex, String franchiseeBankInfoString, String imageCategory, MultipartFile uploadImage) {
        log.trace(" 가맹점 회원 계정 생성 ");
        printUpdateFranchisee();
        String message;
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        boolean checkExistBank = franchiseeBankRepository.existsByFranchiseeEntity(franchiseeEntity);
        if (!(checkExistBank)) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "[Bank Info] doesn't exist.");
        }

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            FranchiseeBankInfo franchiseeBankInfo = objectMapper.readValue(franchiseeBankInfoString, FranchiseeBankInfo.class);
            FranchiseeBankEntity franchiseeBankEntity = franchiseeBankFindService.findByFranchiseeEntity(franchiseeEntity);
            franchiseeBankEntity.updateBankInfo(franchiseeBankInfo);

            if (imageCategory.equals("X")) {
                message = "Bank Info Updated Only";
            } else {
                String delete = s3FileUploader.deleteJpg(franchiseeIndex, imageCategory);
                System.out.println(delete);
                message = s3FileUploader.uploadJpg(franchiseeIndex, imageCategory, uploadImage);
            }
        } catch (Exception e) {
            throw new UnknownException(ExceptionState.UNKNOWN, "Contact Backend Developer");
        }
        franchiseeApplicantFindService.findByFranchiseeEntity(franchiseeEntity).reapply();
        return message;
    }

    @Transactional
    public String uploadImageAndBankInfo(Long franchiseeIndex, String imageCategory, MultipartFile uploadImage) {
        printUpdateFranchisee();
        String message;
        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);
        boolean checkExistBank = franchiseeBankRepository.existsByFranchiseeEntity(franchiseeEntity);
        if (!(checkExistBank)) {
            throw new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "[Bank Info] doesn't exist.");
        }

        try {

            if (imageCategory.equals("X")) {
                message = "Bank Info Updated Only";
            } else {
                String delete = s3FileUploader.deleteJpg(franchiseeIndex, imageCategory);
                message = s3FileUploader.uploadJpg(franchiseeIndex, imageCategory, uploadImage);
            }
        } catch (Exception e) {
            throw new UnknownException(ExceptionState.UNKNOWN, "Contact Backend Developer");
        }
        franchiseeApplicantFindService.findByFranchiseeEntity(franchiseeEntity).reapply();
        return message;
    }

    void printNewFranchisee() {
        System.out.println("========================");
        System.out.println("=====      New     =====");
        System.out.println("=====  Franchisee  =====");
        System.out.println("========================");
    }

    void printUpdateFranchisee() {
        System.out.println("========================");
        System.out.println("=====    Update    =====");
        System.out.println("=====  Franchisee  =====");
        System.out.println("========================");
    }
}
