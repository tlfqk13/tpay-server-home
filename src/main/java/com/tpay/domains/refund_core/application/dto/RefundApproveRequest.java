package com.tpay.domains.refund_core.application.dto;

import com.tpay.commons.custom.CustomValue;
import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RefundApproveRequest {
    private String serviceName;
    private String businessNumber;
    private String franchiseeName;
    private String franchiseeNumber;
    private String sellerName;
    private String storeName;
    private String storeAddress;
    private String storeTel;

    private String name;
    private String nationality;
    private String totalAmount;
    private String passportNumber;

    private String totalRefund;
    private String totalVat;
    private String totalIct;
    private String totalStr;
    private String totalEdut;
    private String totalQuantity;
    private String saleDate;

    private String indList;
    private String indListNow;
    List<RefundProductInfo> productInfoList;

    public static RefundApproveRequest of(OrderEntity orderEntity) {
        List<RefundProductInfo> refundProductInfo = orderEntity.getRefundProductInfoList();
        CustomerEntity customerEntity = orderEntity.getCustomerEntity();
        FranchiseeEntity franchiseeEntity = orderEntity.getFranchiseeEntity();

        return RefundApproveRequest.builder()
            .serviceName(CustomValue.APPLICATION_CODE)
            .nationality(customerEntity.getNation())
            .totalAmount(orderEntity.getTotalAmount())
            .businessNumber(franchiseeEntity.getBusinessNumber())
            .franchiseeName(franchiseeEntity.getStoreName())
            .franchiseeNumber(franchiseeEntity.getMemberNumber())
            .name(customerEntity.getCustomerName())
            .passportNumber(customerEntity.getPassportNumber())
            .indList("1")
            .indListNow("1")
            .productInfoList(refundProductInfo)
            .sellerName(franchiseeEntity.getSellerName())
            .storeAddress(franchiseeEntity.getStoreAddressBasic() + " " + franchiseeEntity.getStoreAddressDetail())
            .storeName(franchiseeEntity.getStoreName())
            .storeTel(franchiseeEntity.getStoreTel())
            .totalEdut("0")
            .totalIct("0")
            .totalQuantity(orderEntity.getTotalQuantity())
            .saleDate(orderEntity.getSaleDate())
            .totalRefund(orderEntity.getTotalRefund())
            .totalStr("0")
            .totalVat(orderEntity.getTotalVat())
            .build();
    }
}
