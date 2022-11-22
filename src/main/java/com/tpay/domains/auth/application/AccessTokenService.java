package com.tpay.domains.auth.application;

import com.tpay.commons.util.IndexInfo;
import com.tpay.domains.auth.domain.EmployeeAccessTokenEntity;
import com.tpay.domains.auth.domain.EmployeeAccessTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeAccessTokenEntity;
import com.tpay.domains.auth.domain.FranchiseeAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccessTokenService {

    private final FranchiseeAccessTokenRepository franchiseeAccessTokenRepository;
    private final EmployeeAccessTokenRepository employeeAccessTokenRepository;

    public Optional<FranchiseeAccessTokenEntity> findByFranchiseeId(Long franchiseeIndex) {
        return franchiseeAccessTokenRepository.findByFranchiseeEntityId(franchiseeIndex);
    }

    public boolean existByBusinessNumber(String businessNumber){
        return franchiseeAccessTokenRepository.existsByFranchiseeEntityBusinessNumber(businessNumber);
    }

    @Transactional
    public void deleteByFranchiseeEntityId (Long franchiseeIndex){
        franchiseeAccessTokenRepository.deleteByFranchiseeEntityId(franchiseeIndex);
    }

    public Optional<EmployeeAccessTokenEntity> findByEmployeeId(Long employeeIndex) {
        return employeeAccessTokenRepository.findByEmployeeEntityId(employeeIndex);
    }

    public boolean existByUserId(String userId){
        return employeeAccessTokenRepository.existsByEmployeeEntityUserId(userId);
    }

    @Transactional
    public void deleteByEmployeeEntityId (Long employeeIndex){
        employeeAccessTokenRepository.deleteByEmployeeEntityId(employeeIndex);
    }

    @Transactional
    public void deleteById(IndexInfo indexInfo) {
        Long index = Long.parseLong(indexInfo.getIndex());
        if (FRANCHISEE == indexInfo.getUserSelector()) {
            franchiseeAccessTokenRepository.deleteByFranchiseeEntityId(index);
        }
        employeeAccessTokenRepository.findByEmployeeEntityId(index);
    }
}

