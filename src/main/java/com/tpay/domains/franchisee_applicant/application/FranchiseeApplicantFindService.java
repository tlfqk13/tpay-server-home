package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FilterSelector;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDto;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tpay.domains.franchisee_applicant.application.dto.FilterSelector.BOTH;
import static com.tpay.domains.franchisee_applicant.application.dto.FilterSelector.IS_READ;

@Service
@RequiredArgsConstructor
@Slf4j
public class FranchiseeApplicantFindService {

    private final FranchiseeApplicantRepository franchiseeApplicantRepository;

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


    public List<FranchiseeApplicantEntity> findByFranchiseeStatus(FranchiseeStatus franchiseeStatus) {
        return franchiseeApplicantRepository.findByFranchiseeStatus(franchiseeStatus);
    }


    // 2022/04/26 조회하려는 컬럼 분리
    public Page<FranchiseeApplicantDto.Response> applicantFilter(Pageable pageable, FilterSelector filterSelector, String value, String searchKeyword) {

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
}