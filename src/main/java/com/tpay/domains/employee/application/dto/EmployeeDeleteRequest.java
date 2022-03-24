package com.tpay.domains.employee.application.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class EmployeeDeleteRequest {
    private List<Long> employeeIndexList;
}
