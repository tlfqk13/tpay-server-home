package com.tpay.domains.refund.application.dto;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.employee.domain.EmployeeEntity;
import lombok.Getter;

@Getter
public class RefundSaveRequest {
    private Long franchiseeIndex;
    private Long employeeIndex;
    private Long customerIndex;
    private String price;
    private UserSelector userSelector;

    public void updateFranchiseeIndex(EmployeeEntity employeeEntity) {
        this.franchiseeIndex = employeeEntity.getFranchiseeEntity().getId();
    }
}
