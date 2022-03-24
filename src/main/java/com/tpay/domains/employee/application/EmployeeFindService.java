package com.tpay.domains.employee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.domains.employee.application.dto.EmployeeFindResponseInterface;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.employee.domain.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeFindService {

    private final EmployeeRepository employeeRepository;

    public EmployeeEntity findByUserId(String userId) {
        return employeeRepository.findByUserId(userId)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "가입 내역이 존재하지 않습니다. 다시 입력해주세요."));
    }

    public Optional<EmployeeEntity> findById(Long employeeIndex) {
        return employeeRepository.findById(employeeIndex);
    }


    public List<EmployeeFindResponseInterface> findAllByFranchiseeId(Long franchiseeIndex) {
        return employeeRepository.findAllByFranchiseeId(franchiseeIndex);
    }
}
