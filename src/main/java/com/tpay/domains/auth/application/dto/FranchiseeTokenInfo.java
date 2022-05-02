package com.tpay.domains.auth.application.dto;

import com.tpay.domains.franchisee_applicant.domain.FranchiseeStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FranchiseeTokenInfo {
    private Long franchiseeIndex;
    private String businessNumber;
    private FranchiseeStatus franchiseeStatus;
    private String rejectReason;
    private String accessToken;
    private String refreshToken;
    private String storeName;
    private LocalDateTime signUpDate;
    private boolean popUp;
    private boolean isActiveSound;
    private boolean isActiveVibration;
    private boolean isConnectedPos;
    private String posType;
}
