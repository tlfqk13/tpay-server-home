package com.tpay.domains.employee.application;


import com.tpay.commons.exception.ExceptionState;
import com.tpay.commons.exception.detail.InvalidPasswordException;
import com.tpay.commons.regex.RegExType;
import com.tpay.commons.regex.RegExUtils;
import com.tpay.domains.employee.application.dto.EmployeeRegistrationRequest;
import com.tpay.domains.employee.domain.EmployeeEntity;
import com.tpay.domains.employee.domain.EmployeeRepository;
import com.tpay.domains.franchisee.application.FranchiseeFindService;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeRegistrationService {

    private final EmployeeRepository employeeRepository;
    private final FranchiseeFindService franchiseeFindService;
    private final PasswordEncoder passwordEncoder;

    public EmployeeEntity registration(Long franchiseeIndex, EmployeeRegistrationRequest employeeRegistrationRequest) {


        String password = employeeRegistrationRequest.getPassword();
        String passwordCheck = employeeRegistrationRequest.getPasswordCheck();
        if (!validPassword(password, passwordCheck)) {
            throw new InvalidPasswordException(ExceptionState.INVALID_PASSWORD, "Invalid Password(regEx or passCheck not matched)");
        }

        FranchiseeEntity franchiseeEntity = franchiseeFindService.findByIndex(franchiseeIndex);

        EmployeeEntity employeeEntity = EmployeeEntity.builder()
            .name(employeeRegistrationRequest.getName())
            .userId(employeeRegistrationRequest.getUserId())
            .password(passwordEncoder.encode(password))
            .franchiseeEntity(franchiseeEntity)
            .isDelete(false)
            .build();

        return employeeRepository.save(employeeEntity);
    }

    static boolean validPassword(String password, String passwordCheck) {
        boolean equalEach = password.equals(passwordCheck);

        RegExUtils regExUtils = new RegExUtils();
        boolean validate = regExUtils.validate(RegExType.PASSWORD, password);

        return equalEach && validate;
    }
}
