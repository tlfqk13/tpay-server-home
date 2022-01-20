package com.tpay.domains.franchisee.application.dto;

import lombok.Getter;

@Getter
public class FranchiseeSignUpRequest {

  //계정정보
  private String sellerName; // 판매자이름 K1: 성명(대표자명)
  //2022.01 K1프로젝트에 의해 판매자 연락처와 매장번호가 분리되었으나, 이미 구현된 기능이 많아 해당 컬럼은 계속 storeTel
  private String storeTel; // 판매자 연락처 K1: 휴대전화번호
  private String businessNumber; // 사업자등록번호
  private String email; // email 주소
  private String password; // 비밀번호


  //매장 정보
  private String storeName; // 판매자상호 K1: 상호(법인명)
  private String signboard; // 간판명
  private String storeAddressNumber; // 판매자주소 K1: 사업장 주소 중 우편번호
  private String storeAddressBasic; // 판매자주소 K1: 사업장 주소 중 기본주소
  private String storeAddressDetail; // 판매자주소 K1: 사업장 주소 중 상세주소
  private String productCategory; // 판매 상품 종목
  private String storeNumber; // 매장 전화번호

  private String isTaxRefundShop; //사후면세점 가입여부
}
