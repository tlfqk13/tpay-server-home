package com.tpay.domains.point.application.dto;


import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.point.domain.PointEntity;
import com.tpay.domains.point.domain.PointStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminPointInfo {
        private Long pointsIndex;
        private PointStatus pointStatus;
        private String businessNumber;
        private String storeName;
        private String sellerName;
        private String requestedDate;
        private Long amount;
        private Boolean isRead;

        public AdminPointInfo(PointEntity pointEntity){
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

