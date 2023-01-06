package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FilterSelector;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantDto;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantFindResponse;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    // 2022/07/21 관리자페이지 페이징 기능 개발
    public FranchiseeApplicantFindResponse findAll(int page, String searchKeyword) {
        PageRequest pageRequest = PageRequest.of(page, 10);
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


    public List<FranchiseeApplicantEntity> findByFranchiseeStatus(FranchiseeStatus franchiseeStatus) {
        return franchiseeApplicantRepository.findByFranchiseeStatus(franchiseeStatus);
    }


    // 2022/04/26 조회하려는 컬럼 분리
    public FranchiseeApplicantFindResponse applicantFilter(FilterSelector filterSelector, String value, int page, String searchKeyword) {

        PageRequest pageRequest = PageRequest.of(page, 10);
        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);

        FranchiseeStatus franchiseeStatus;
        Page<FranchiseeApplicantDto.Response> response;

        // TODO: 2022/11/11 가맹점 신청상태, 알림상태, 둘 다

        if (filterSelector.equals(IS_READ)) {
            response = franchiseeApplicantRepository.findFranchiseeAllFilter(pageRequest,searchKeyword,false,isBusinessNumber);
        } else if (filterSelector.equals(BOTH)) {
            franchiseeStatus = FranchiseeStatus.valueOf(value);
            response = franchiseeApplicantRepository.findFranchiseeAllFilter(pageRequest, searchKeyword, franchiseeStatus,false,isBusinessNumber);
        } else {
            franchiseeStatus = FranchiseeStatus.valueOf(value);
            response = franchiseeApplicantRepository.findFranchiseeAllFilter(pageRequest, searchKeyword, franchiseeStatus,true,isBusinessNumber);
        }

        List<FranchiseeApplicantInfo> franchiseeApplicantInfoList =
                response.stream().map(FranchiseeApplicantInfo::toResponse).collect(Collectors.toList());

        int totalPage = response.getTotalPages();
        if (totalPage != 0) {
            totalPage = totalPage - 1;
        }
        return FranchiseeApplicantFindResponse.builder()
                .totalPage(totalPage)
                .franchiseeApplicantInfoList(franchiseeApplicantInfoList)
                .build();
    }
}