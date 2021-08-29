package com.tpay.domains.franchisee.application.dto;

import lombok.Getter;

@Getter
public class FranchiseeSignUpRequest {
  private String businessNumber; // 사업자등록번호
  private String storeName; // 판매자상호
  private String storeAddress; // 판매자주소
  private String sellerName; // 판매자이름
  private String storeTel; // 판매자 연락처
  private String productCategory; // 판매품목
  private String password; // 비밀번호
}
