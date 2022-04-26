package com.tpay.domains.auth.application.dto;

import com.tpay.commons.util.UserSelector;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.*;

import java.time.LocalDateTime;

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

    //2. EmployeeTokenInfo
    private Long employeeIndex;
    private String userId;
    private String name;
    ;
    private LocalDateTime registeredDate;


    //3. common
    private String accessToken;
    private String refreshToken;
    private UserSelector userSelector;
    private Boolean isActiveSound;
    private Boolean isActiveVibration;
}
