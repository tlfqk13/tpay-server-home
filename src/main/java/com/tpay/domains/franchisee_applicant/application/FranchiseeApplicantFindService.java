package com.tpay.domains.franchisee_applicant.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.franchisee_applicant.application.dto.FilterSelector;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantFindResponse;
import com.tpay.domains.franchisee_applicant.application.dto.FranchiseeApplicantInfo;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantRepository;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tpay.domains.franchisee_applicant.application.dto.FilterSelector.FRANCHISEE_STATUS;

@Service
@RequiredArgsConstructor
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


    public List<FranchiseeApplicantEntity> findByFranchiseeStatus(FranchiseeStatus franchiseeStatus) {
        return franchiseeApplicantRepository.findByFranchiseeStatus(franchiseeStatus);
    }


    // 2022/04/26 조회하려는 컬럼 분리
    public FranchiseeApplicantFindResponse applicantFilter(FilterSelector filterSelector, String value, int page, String searchKeyword) {
        List<Boolean> booleanList = new ArrayList<>(List.of(false));
        List<FranchiseeStatus> franchiseeStatusList = new ArrayList<>();
        Page<FranchiseeApplicantEntity> franchiseeApplicantEntityList;
        PageRequest pageRequest = PageRequest.of(page, 15);
        boolean isBusinessNumber = searchKeyword.chars().allMatch(Character::isDigit);

        if (!searchKeyword.isEmpty()) {
            if (filterSelector.equals(FRANCHISEE_STATUS)) {
                booleanList.add(true);
                franchiseeStatusList.add(FranchiseeStatus.valueOf(value));
                if (isBusinessNumber) {
                    franchiseeApplicantEntityList = franchiseeApplicantRepository.filterAndBusinessNumber(booleanList, franchiseeStatusList, pageRequest, searchKeyword);
                } else {
                    franchiseeApplicantEntityList = franchiseeApplicantRepository.filterAndStoreName(booleanList, franchiseeStatusList, pageRequest, searchKeyword);
                }
            } else {
                if (isBusinessNumber) {
                    franchiseeApplicantEntityList = franchiseeApplicantRepository.filterIsReadAndBusinessNumber(booleanList.get(0), pageRequest, searchKeyword);
                } else {
                    franchiseeApplicantEntityList = franchiseeApplicantRepository.filterIsReadAndStoreName(booleanList.get(0), pageRequest, searchKeyword);
                }
            }
        } else {
            if (filterSelector.equals(FRANCHISEE_STATUS)) {
                booleanList.add(true);
                franchiseeStatusList.add(FranchiseeStatus.valueOf(value));
                franchiseeApplicantEntityList = franchiseeApplicantRepository.findByIsReadInAndFranchiseeStatusInOrderByIdDesc(booleanList, franchiseeStatusList, pageRequest);
            } else {
                franchiseeApplicantEntityList = franchiseeApplicantRepository.findByIsReadOrderByIdDesc(booleanList.get(0), pageRequest);
            }
        }

        List<FranchiseeApplicantInfo> franchiseeApplicantInfoList = franchiseeApplicantEntityList.stream().map(FranchiseeApplicantInfo::toResponse).collect(Collectors.toList());
        int totalPage = franchiseeApplicantEntityList.getTotalPages();
        if (totalPage != 0) {
            totalPage = totalPage - 1;
        }
        FranchiseeApplicantFindResponse franchiseeApplicantFindResponse = FranchiseeApplicantFindResponse.builder()
                .totalPage(totalPage)
                .franchiseeApplicantInfoList(franchiseeApplicantInfoList)
                .build();

        return franchiseeApplicantFindResponse;
    }
}