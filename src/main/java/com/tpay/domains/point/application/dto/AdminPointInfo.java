package com.tpay.domains.point.application.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminPointInfo {
    private Long pointsIndex;
    private PointStatus pointStatus;
    private String businessNumber;
    private String storeName;
    private String sellerName;
    private String requestedDate;
    private Long amount;
    private Boolean isRead;

    @QueryProjection
    public AdminPointInfo(Long pointsIndex, PointStatus pointStatus, String businessNumber,
                          String storeName, String sellerName, String requestedDate,
                          Long amount, Boolean isRead) {

        this.pointsIndex = pointsIndex;
        this.pointStatus = pointStatus;
        this.businessNumber = businessNumber;
        this.storeName = storeName;
        this.sellerName = sellerName;
        this.requestedDate = requestedDate;
        this.amount = amount;
        this.isRead = isRead;

    }

    public AdminPointInfo(PointEntity pointEntity) {
        this.pointsIndex = pointEntity.getId();
        this.pointStatus = pointEntity.getPointStatus();
        this.businessNumber = pointEntity.getFranchiseeEntity().getBusinessNumber();
        this.storeName = pointEntity.getFranchiseeEntity().getStoreName();
        this.sellerName = pointEntity.getFranchiseeEntity().getSellerName();
        this.requestedDate = pointEntity.getCreatedDate().toString();
        this.isRead = pointEntity.getIsRead();
        this.amount = pointEntity.getChange();
    }
}

