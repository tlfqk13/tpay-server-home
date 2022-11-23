package com.tpay.domains.refund.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundSaveRequest {
//    private Long franchiseeIndex;
//    private Long employeeIndex;
    private Long customerIndex;
    private String price;
    private String refund;
//    private UserSelector userSelector;
    private Device device;

//    public void updateFranchiseeIndex(EmployeeEntity employeeEntity) {
//        this.franchiseeIndex = employeeEntity.getFranchiseeEntity().getId();
//    }
}
