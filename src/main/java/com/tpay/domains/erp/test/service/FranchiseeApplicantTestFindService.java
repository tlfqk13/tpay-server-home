package com.tpay.domains.franchisee_applicant_test.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.UnknownException;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.tpay.domains.franchisee_applicant.application.dto.FilterSelector.BOTH;
import static com.tpay.domains.franchisee_applicant.application.dto.FilterSelector.IS_READ;

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

    public FranchiseeApplicantFindResponse findAll(int page, String searchKeyword) {
        PageRequest pageRequest = PageRequest.of(page, 15);
        Page<FranchiseeApplicantEntity> franchiseeApplicantEntityPage;

        if (!searchKeyword.isEmpty()) {
            boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);
            if (isBusinessNumber) {
                franchiseeApplicantEntityPage = franchiseeApplicantRepository.findByFranchiseeEntityBusinessNumberContaining(pageRequest, searchKeyword);
            } else {
                franchiseeApplicantEntityPage = franchiseeApplicantRepository.findByFranchiseeEntityStoreNameContaining(pageRequest, searchKeyword);
            }
        } else {
            franchiseeApplicantEntityPage = franchiseeApplicantRepository.findAllByOrderByIdDesc(pageRequest);
        }

        List<FranchiseeApplicantInfo> franchiseeApplicantInfoList = franchiseeApplicantEntityPage.stream().map(FranchiseeApplicantInfo::toResponse).collect(Collectors.toList());
        int totalPage = franchiseeApplicantEntityPage.getTotalPages();
        if (totalPage != 0) {
            totalPage = totalPage - 1;
        }
        FranchiseeApplicantFindResponse franchiseeApplicantFindResponse = FranchiseeApplicantFindResponse.builder()
                .totalPage(totalPage)
                .franchiseeApplicantInfoList(franchiseeApplicantInfoList)
                .build();

        return franchiseeApplicantFindResponse;
    }


    public FranchiseeApplicantEntity findByBusinessNumber(String businessNumber) {
        businessNumber = businessNumber.replaceAll("-", "");

        FranchiseeApplicantEntity franchiseeApplicantEntity =
                franchiseeApplicantRepository
                        .findByFranchiseeEntityBusinessNumber(businessNumber)
                        .orElseThrow(
                                () ->
                                        new InvalidParameterException(
                                                ExceptionState.INVALID_PARAMETER, "가입 내역이 존재하지 않습니다. 다시 입력해주세요."));

        return franchiseeApplicantEntity;
    }

    // TODO: 2022/04/26 조회하려는 컬럼 분리
    public FranchiseeApplicantFindResponse applicantFilterTest(FilterSelector filterSelector, String value, int page, String searchKeyword) {
        PageRequest pageRequest = PageRequest.of(page, 15);
        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);
        Page<FranchiseeApplicantEntity> franchiseeApplicantEntityList = null;

        FranchiseeStatus franchiseeStatus;
        Page<FranchiseeApplicantDto.Response> response;

        // TODO: 2022/11/11 가맹점 신청상태, 알림상태, 둘 다

        if (filterSelector.equals(IS_READ)) {
            response = franchiseeApplicantRepository.findBusinessNumberFromDsl(pageRequest,searchKeyword,false,isBusinessNumber);
            log.trace(" @@  here @@ " );
        } else if (filterSelector.equals(BOTH)) {
            franchiseeStatus = FranchiseeStatus.valueOf(value);
            response = franchiseeApplicantRepository.findBusinessNumberFromDsl(pageRequest, searchKeyword, franchiseeStatus,false,isBusinessNumber);
        } else {
            franchiseeStatus = FranchiseeStatus.valueOf(value);
            response = franchiseeApplicantRepository.findBusinessNumberFromDsl(pageRequest, searchKeyword, franchiseeStatus,true,isBusinessNumber);
        }

        List<FranchiseeApplicantInfo> franchiseeApplicantInfoList =
                response.stream().map(FranchiseeApplicantInfo::toResponse).collect(Collectors.toList());

        int totalPage = response.getTotalPages();
        if (totalPage != 0) {
            totalPage = totalPage - 1;
        }

        FranchiseeApplicantFindResponse franchiseeApplicantFindResponse = FranchiseeApplicantFindResponse.builder()
                .totalPage(totalPage)
                .franchiseeApplicantInfoList(franchiseeApplicantInfoList)
                .build();

        return franchiseeApplicantFindResponse;
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
            if ("O".equals(request.getRefundAfterShop())) {
                franchiseeEntity.updateAfterRefund(true);
            } else {
                franchiseeEntity.updateAfterRefund(false);
            }
        } catch (InvalidParameterException e) {
            franchiseeBankEntity = FranchiseeBankEntity.builder().build();
        } catch (Exception e) {
            throw new UnknownException(ExceptionState.UNKNOWN, "detailFranchiseeInfo data parsing error");
        }

        log.trace("Franchisee Update : {} ", franchiseeEntityUpdate.getStoreName());
        return new FranchiseeApplicantDetailUpdateResponse(franchiseeEntityUpdate, franchiseeBankEntity, taxFreeStoreNumberUpdate);
    }
}