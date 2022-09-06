package com.tpay.domains.employee.application;

import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidParameterException;
import com.tpay.commons.exception.detail.InvalidPasswordException;
import com.tpay.domains.employee.application.dto.EmployeeUpdateRequest;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.employee.domain.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeUpdateService {

    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public boolean update(Long employeeIndex, EmployeeUpdateRequest employeeUpdateRequest) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeIndex)
            .orElseThrow(() -> new InvalidParameterException(ExceptionState.INVALID_PARAMETER, "employee doesn't exists"));

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
