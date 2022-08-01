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

        public static AdminPointInfo toResponse(PointEntity pointEntity){
                FranchiseeEntity franchiseeEntity = pointEntity.getFranchiseeEntity();
                return AdminPointInfo.builder()
                        .pointsIndex(pointEntity.getId())
                        .pointStatus(pointEntity.getPointStatus())
                        .businessNumber(franchiseeEntity.getBusinessNumber())
                        .storeName(franchiseeEntity.getStoreName())
                        .sellerName(franchiseeEntity.getSellerName())
                        .requestedDate(pointEntity.getCreatedDate().toString())
                        .amount(pointEntity.getChange())
                        .isRead(pointEntity.getIsRead())
                        .build();
        }
}

