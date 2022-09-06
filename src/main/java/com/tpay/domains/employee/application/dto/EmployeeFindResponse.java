package com.tpay.domains.employee.application.dto;

import com.tpay.domains.employee.domain.EmployeeEntity;
import lombok.Getter;

@Getter
public class EmployeeFindResponse {
    private final Long employeeIndex;
    private final String name;
    private final String userId;

    public EmployeeFindResponse(EmployeeEntity employeeEntity) {
        this.employeeIndex = employeeEntity.getId();
        this.name = employeeEntity.getName();
        this.userId = employeeEntity.getUserId();
    }
}
