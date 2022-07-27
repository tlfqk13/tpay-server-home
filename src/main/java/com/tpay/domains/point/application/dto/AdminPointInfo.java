package com.tpay.domains.point.application.dto;


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

        public AdminPointInfo(PointEntity point) {
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
