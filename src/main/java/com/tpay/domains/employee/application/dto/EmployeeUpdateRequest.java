package com.tpay.domains.employee.application.dto;

import lombok.Getter;

@Getter
public class EmployeeUpdateRequest {

    private Long employeeIndex;
    private String name;
    private String password;
    private String passwordCheck;
}
