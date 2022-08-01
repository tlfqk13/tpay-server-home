package com.tpay.domains.point.application.dto;


import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminPointResponse {
    Long pointsIndex;
    PointStatus pointStatus;
    String businessNumber;
    String storeName;
    String sellerName;
    String requestedDate;
    Long amount;
    Boolean isRead;

    public AdminPointResponse(PointEntity point) {
        this.pointsIndex = point.getId();
        this.pointStatus = point.getPointStatus();
        this.businessNumber = point.getFranchiseeEntity().getBusinessNumber();
        this.storeName = point.getFranchiseeEntity().getStoreName();
        this.sellerName = point.getFranchiseeEntity().getSellerName();
        this.requestedDate = point.getCreatedDate().toString();
        this.amount = point.getChange();
        this.isRead =point.getIsRead();
    }
}
