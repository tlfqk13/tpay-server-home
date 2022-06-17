package com.tpay.domains.auth.application.dto;


import com.tpay.commons.util.UserSelector;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeTokenInfo implements TokenInfoInterface{
    private Long employeeIndex;
    private String userId;
    private String name;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime registeredDate;
    private Long franchiseeIndex;
    private FranchiseeStatus franchiseeStatus;
    private boolean isActiveSound;
    private boolean isActiveVibration;
    private boolean isConnectedPos;

    @Override
    public String getBusinessNumber() {
        return null;
    }

    @Override
    public String getRejectReason() {
        return null;
    }

    @Override
    public String getStoreName() {
        return null;
    }

    @Override
    public LocalDateTime getSignUpDate() {
        return null;
    }

    @Override
    public boolean isPopUp() {
        return false;
    }

    @Override
    public String getPosType() {
        return null;
    }

    @Override
    public UserSelector getUserSelector() {
        return UserSelector.EMPLOYEE;
    }
}
