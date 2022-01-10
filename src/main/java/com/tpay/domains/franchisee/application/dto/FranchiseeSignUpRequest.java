package com.tpay.domains.franchisee.application.dto;

import lombok.Getter;

@Getter
public class FranchiseeSignUpRequest {
  private String businessNumber; // 사업자등록번호
  private String storeName; // 판매자상호
  private String storeAddress; // 판매자주소
  private String sellerName; // 판매자이름

  //2022.01 K1프로젝트에 의해 판매자 연락처와 매장번호가 분리되었으나, 이미 구현된 기능이 많아 해당 컬럼은 계속 storeTel
  private String storeTel; // 판매자 연락처
  private String productCategory; // 판매품목(종목)
  private String password; // 비밀번호

  private String businessType; // 업종
  private String signboard; // 간판명
  private String storeNumber; // 매장번호
  private String email; // email 주소
}
