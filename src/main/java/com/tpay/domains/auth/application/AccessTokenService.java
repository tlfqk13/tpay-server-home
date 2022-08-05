package com.tpay.domains.auth.application;

import com.tpay.domains.auth.domain.EmployeeAccessTokenEntity;
import com.tpay.domains.auth.domain.EmployeeAccessTokenRepository;
import com.tpay.domains.auth.domain.FranchiseeAccessTokenEntity;
import com.tpay.domains.auth.domain.FranchiseeAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.tpay.commons.util.converter.NumberFormatConverter.addBarToBusinessNumber;
import static com.tpay.commons.util.converter.NumberFormatConverter.businessNumberReplace;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final FranchiseeAccessTokenRepository franchiseeAccessTokenRepository;
    private final EmployeeAccessTokenRepository employeeAccessTokenRepository;

    public Optional<FranchiseeAccessTokenEntity> findByFranchiseeId(Long franchiseeIndex) {
        return franchiseeAccessTokenRepository.findByFranchiseeEntityId(franchiseeIndex);
    }

    public boolean findByBusinessNumber(String businessNumber){
        Optional<FranchiseeAccessTokenEntity> result = franchiseeAccessTokenRepository.findByFranchiseeEntityBusinessNumber(businessNumber);
        return result.isPresent();
    }

    public void deleteByFranchiseeEntityId (Long franchiseeIndex){
        franchiseeAccessTokenRepository.deleteByFranchiseeEntityId(franchiseeIndex);
    }



    public Optional<EmployeeAccessTokenEntity> findByEmployeeId(Long employeeIndex) {
        return employeeAccessTokenRepository.findByEmployeeEntityId(employeeIndex);
    }

    public boolean findByEmployeeEntity_UserId(String userId){
        Optional<EmployeeAccessTokenEntity> result = employeeAccessTokenRepository.findByEmployeeEntity_UserId(userId);
        return result.isPresent();
    }

    public void deleteByEmployeeEntityId (Long employeeIndex){
        employeeAccessTokenRepository.deleteByEmployeeEntityId(employeeIndex);
    }
}

