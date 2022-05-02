package com.tpay.domains.franchisee.application.dto;

import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FindAllResponse {
    private Long franchiseeIndex;
    private String memberName;
    private String memberNumber;
    private String businessNumber;
    private String storeName;
    private String sellerName;
    private String sellerTel;
    private String productCategory;

    public static FindAllResponse of(FranchiseeEntity franchiseeEntity) {
        return FindAllResponse.builder()
            .franchiseeIndex(franchiseeEntity.getId())
            .memberName(franchiseeEntity.getMemberName())
            .memberNumber(franchiseeEntity.getMemberNumber())
            .businessNumber(franchiseeEntity.getBusinessNumber())
            .storeName(franchiseeEntity.getStoreName())
            .sellerName(franchiseeEntity.getSellerName())
            .sellerTel(franchiseeEntity.getStoreTel())
            .productCategory(franchiseeEntity.getProductCategory())
            .build();
    }
}
