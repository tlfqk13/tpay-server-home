package com.tpay.domains.refund_core.application.dto;

import com.tpay.domains.customer.domain.CustomerEntity;
import com.tpay.domains.franchisee.domain.FranchiseeEntity;
import com.tpay.domains.order.domain.OrderEntity;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundApproveRequest {
  private String businessNumber;
  private String franchiseeName;
  private String franchiseeNumber;
  private String sellerName;
  private String storeName;
  private String storeAddress;
  private String storeTel;

  private String productList;
  private String productListNow;

  private String name;
  private String nationality;
  private String amount;
  private String passportNumber;

  private String totalRefund;
  private String totalVAT;
  private String totalIct;
  private String totalStr;
  private String totalEdut;
  private String totalQuantity;

  List<RefundProductInfo> refundProductInfo;

  public static RefundApproveRequest of(OrderEntity orderEntity) {
    List<RefundProductInfo> refundProductInfo = orderEntity.getRefundProductInfoList();
    CustomerEntity customerEntity = orderEntity.getCustomerEntity();
    FranchiseeEntity franchiseeEntity = orderEntity.getFranchiseeEntity();

    return RefundApproveRequest.builder()
        .nationality(customerEntity.getNation())
        .amount(orderEntity.getTotalAmount())
        .businessNumber(franchiseeEntity.getBusinessNumber())
        .franchiseeName(franchiseeEntity.getStoreName())
        .franchiseeNumber(franchiseeEntity.getMemberNumber())
        .name(customerEntity.getCustomerName())
        .passportNumber(customerEntity.getPassportNumber())
        .productList("1")
        .productListNow("1")
        .refundProductInfo(refundProductInfo)
        .sellerName(franchiseeEntity.getSellerName())
        .storeAddress(franchiseeEntity.getStoreAddress())
        .storeName(franchiseeEntity.getStoreName())
        .storeTel(franchiseeEntity.getStoreTel())
        .totalEdut("0")
        .totalIct("0")
        .totalQuantity(orderEntity.getTotalQuantity())
        .totalRefund(orderEntity.getTotalRefund())
        .totalStr("0")
        .totalVAT(orderEntity.getTotalVat())
        .build();
  }
}
