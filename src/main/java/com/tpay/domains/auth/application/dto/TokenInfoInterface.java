package com.tpay.domains.auth.application.dto;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;

import java.time.LocalDateTime;

public interface TokenInfoInterface {

    Long getFranchiseeIndex();
    Long getEmployeeIndex();
    String getBusinessNumber();
    FranchiseeStatus getFranchiseeStatus();
    String getRejectReason();
    String getAccessToken();
    String getRefreshToken();
    String getStoreName();
    LocalDateTime getSignUpDate();
    boolean isPopUp();
    boolean isActiveSound();
    boolean isActiveVibration();
    boolean isConnectedPos();
    String getPosType();

    String getUserId();
    String getName();
    LocalDateTime getRegisteredDate();

    UserSelector getUserSelector();
}
