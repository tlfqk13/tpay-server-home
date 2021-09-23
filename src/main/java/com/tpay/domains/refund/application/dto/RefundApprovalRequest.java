package com.tpay.domains.refund.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RefundApprovalRequest {
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
  private String orderNumber;
  private String amount;
  private String passportNumber;

  private String totalRefund;
  private String totalVAT;
  private String totalIct;
  private String totalStr;
  private String totalEdut;
  private String totalQuantity;

  List<RefundProductInfo> refundProductInfo;
}
