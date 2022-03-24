package com.tpay.domains.certifications.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.AlreadyExistsException;
import com.tpay.domains.franchisee.domain.FranchiseeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessNumberValidationService {

    private final FranchiseeRepository franchiseeRepository;

    public void valid(Long businessNumber) {
        if (franchiseeRepository.existsByBusinessNumber(businessNumber.toString().replaceAll("-", ""))) {
            throw new AlreadyExistsException(ExceptionState.ALREADY_EXISTS, "Franchisee Already Exists");
        }
    }
}
