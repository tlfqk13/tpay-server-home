package com.tpay.domains.auth.application.dto;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.*;

import java.time.LocalDateTime;

import static com.tpay.commons.util.UserSelector.FRANCHISEE;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignInTokenInfo {

    //1. FranchiseeTokenInfo
    private Long franchiseeIndex;
    private String businessNumber;
    private FranchiseeStatus franchiseeStatus;
    private String rejectReason;
    private LocalDateTime signUpDate;
    private boolean popUp;
    private String storeName;
    private Boolean isConnectedPos;
    private String posType;

    //2. EmployeeTokenInfo
    private Long employeeIndex;
    private String userId;
    private String name;
    private LocalDateTime registeredDate;


    //3. common
    private String accessToken;
    private String refreshToken;
    private UserSelector userSelector;
    private Boolean isActiveSound;
    private Boolean isActiveVibration;


    public SignInTokenInfo(TokenInfoInterface tokenInfoInterface){
        this.franchiseeIndex = tokenInfoInterface.getFranchiseeIndex();
        this.businessNumber = tokenInfoInterface.getBusinessNumber();
        this.franchiseeStatus = tokenInfoInterface.getFranchiseeStatus();
        this.rejectReason = tokenInfoInterface.getRejectReason();
        this.signUpDate = tokenInfoInterface.getSignUpDate();
        this.popUp = tokenInfoInterface.isPopUp();
        this.storeName = tokenInfoInterface.getStoreName();
        this.isConnectedPos = tokenInfoInterface.isConnectedPos();
        this.posType = tokenInfoInterface.getPosType();

        this.employeeIndex = tokenInfoInterface.getEmployeeIndex();
        this.userId = tokenInfoInterface.getUserId();
        this.name = tokenInfoInterface.getName();
        this.registeredDate = tokenInfoInterface.getRegisteredDate();

        this.accessToken = tokenInfoInterface.getAccessToken();
        this.refreshToken = tokenInfoInterface.getRefreshToken();
        this.userSelector = tokenInfoInterface.getUserSelector();
        this.isActiveSound = tokenInfoInterface.isActiveSound();
        this.isActiveVibration = tokenInfoInterface.isActiveVibration();
    }
}
