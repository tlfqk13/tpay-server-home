package com.tpay.domains.employee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPasswordException;
import com.tpay.domains.employee.application.dto.EmployeeUpdateRequest;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.employee.domain.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeUpdateService {

    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public boolean update(EmployeeUpdateRequest employeeUpdateRequest) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeUpdateRequest.getEmployeeIndex())
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "employee doesn't exists"));
        log.warn("@@@@@@@@@@@ 직원 정보 변경 @@@@@@@@@@@@@");
        log.warn(employeeUpdateRequest.getEmployeeIndex().toString());
        log.warn("@@@@@@@@@@@ 직원 정보 변경 @@@@@@@@@@@@@");
        String name = employeeUpdateRequest.getName();
        String password = employeeUpdateRequest.getPassword();
        if (password.equals("")) {
            employeeEntity.updateInfo(name);
            return true;
        } else {
            String passwordCheck = employeeUpdateRequest.getPasswordCheck();
            String passwordEncoded = passwordEncoder.encode(password);
            if (!password.equals(passwordCheck)) {
                throw new InvalidPasswordException(ExceptionState.INVALID_PASSWORD, "Password and passwordCheck Mismatch");
            }
            employeeEntity.updateInfo(name, passwordEncoded);
        }

        return true;
    }
}
