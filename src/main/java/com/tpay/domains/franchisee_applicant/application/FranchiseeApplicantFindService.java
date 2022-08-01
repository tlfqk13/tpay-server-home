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
import com.tpay.domains.push.application.dto.AdminPushDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tpay.domains.franchisee_applicant.application.dto.FilterSelector.FRANCHISEE_STATUS;
import static com.tpay.domains.franchisee_applicant.application.dto.FilterSelector.IS_READ;

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

    public List<FranchiseeApplicantInfo> findAll() {
        List<FranchiseeApplicantEntity> franchiseeApplicantEntityList = franchiseeApplicantRepository.findAllByOrderByIdDesc();
        return franchiseeApplicantEntityList.stream().map(FranchiseeApplicantInfo::toResponse).collect(Collectors.toList());
    }

    // TODO: 2022/07/21 관리자페이지 페이징 기능 개발
    public FranchiseeApplicantFindResponse findAll(int page){
        PageRequest pageRequest = PageRequest.of(page,15);
        Page<FranchiseeApplicantEntity> franchiseeApplicantEntityPage = franchiseeApplicantRepository.findAllByOrderByIdDesc(pageRequest);
        List<FranchiseeApplicantInfo> franchiseeApplicantInfoList = franchiseeApplicantEntityPage.stream().map(FranchiseeApplicantInfo::toResponse).collect(Collectors.toList());

        FranchiseeApplicantFindResponse franchiseeApplicantFindResponse = FranchiseeApplicantFindResponse.builder()
                .totalPage(franchiseeApplicantEntityPage.getTotalPages()-1)
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


    // TODO: 2022/04/26 조회하려는 컬럼 분리
    public List<FranchiseeApplicantInfo> applicantFilter(FilterSelector filterSelector, String value) {
        List<Boolean> booleanList = new ArrayList<>(List.of(false));
        List<FranchiseeStatus> franchiseeStatusList = new ArrayList<>();
        List<FranchiseeApplicantEntity> franchiseeApplicantEntityList;
        if (filterSelector.equals(FRANCHISEE_STATUS)) {
            booleanList.add(true);
            franchiseeStatusList.add(FranchiseeStatus.valueOf(value));
            franchiseeApplicantEntityList = franchiseeApplicantRepository.findByIsReadInAndFranchiseeStatusInOrderByIdDesc(booleanList, franchiseeStatusList);
        } else if (filterSelector.equals(IS_READ)) {
            franchiseeApplicantEntityList = franchiseeApplicantRepository.findByIsReadOrderByIdDesc(booleanList.get(0));
        } else {
            franchiseeStatusList.add(FranchiseeStatus.valueOf(value));
            franchiseeApplicantEntityList = franchiseeApplicantRepository.findByIsReadInAndFranchiseeStatusInOrderByIdDesc(booleanList, franchiseeStatusList);
        }

        if (franchiseeApplicantEntityList.isEmpty()) {
            return new ArrayList<>();
        }
        return franchiseeApplicantEntityList.stream().map(FranchiseeApplicantInfo::toResponse).collect(Collectors.toList());
    }
}
