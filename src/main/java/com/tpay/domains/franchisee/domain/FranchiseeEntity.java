package com.tpay.domains.franchisee.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.point.domain.SignType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "franchisee")
@Entity
@ToString
public class FranchiseeEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "memNm", length = 50, nullable = false)
  private String memberName;

  @NotNull
  @Column(name = "memNo", length = 20, nullable = false)
  private String memberNumber;

  @NotNull
  @Column(name = "bizNo", length = 10, nullable = false)
  private String businessNumber;

  @NotNull
  @Column(name = "storeNm", length = 50, nullable = false)
  private String storeName;

  @NotNull
  @Column(name = "storeAddr", length = 140, nullable = false)
  private String storeAddress;

  @NotNull
  @Column(name = "selNm", length = 40, nullable = false)
  private String sellerName;

  @NotNull
  @Column(name = "storeTel", length = 26, nullable = false)
  private String storeTel;

  @NotNull
  @Column(name = "prdNm", length = 100, nullable = false)
  private String productCategory;

  @NotNull private String password;

  private long balance;

  @Column(name = "businessType")
  private String businessType;

  @Column(name = "signboard")
  private String signboard; // 간판명

  @Column(name = "storeNumber")
  private String storeNumber; // 매장번호

  @Column(name = "email")
  private String email; // email 주소

  @Builder
  public FranchiseeEntity(
      String businessNumber,
      String storeName,
      String storeAddress,
      String sellerName,
      String storeTel,
      String productCategory,
      String password,

      String businessType,
      String signboard,
      String storeNumber,
      String email
  ) {
    this.memberName = "";
    this.memberNumber = "";
    this.businessNumber = businessNumber.replaceAll("-", "");
    this.storeName = storeName;
    this.storeAddress = storeAddress;
    this.sellerName = sellerName;
    this.storeTel = storeTel;
    this.productCategory = productCategory;
    this.password = password;
    this.balance = 0;

    this.businessType = businessType;
    this.signboard = signboard;
    this.storeNumber = storeNumber;
    this.email = email;
  }

  public FranchiseeEntity changeBalance(SignType signType, long change) {
    this.balance += signType == SignType.POSITIVE ? change : -change;
    if (balance < 0) {
      throw new IllegalArgumentException("Balance should not be negative.");
    }
    return this;
  }

  public FranchiseeEntity resetPassword(String password) {
    this.password = password;
    return this;
  }

  public void modifyInfo(
      String storeName, String storeAddress, String businessNumber, String productCategory, String businessType, String signboard, String storeNumber, String email) {
    this.storeName = storeName;
    this.storeAddress = storeAddress;
    this.businessNumber = businessNumber.replaceAll("-", "");
    this.productCategory = productCategory;
    this.businessType = businessType;
    this.signboard = signboard;
    this.storeNumber = storeNumber;
    this.email = email;
  }

  public void memberInfo(String memberName, String memberNumber) {
    this.memberName = memberName;
    this.memberNumber = memberNumber;
  }

  public boolean isValidUser(String name, String phoneNumber) {
    boolean isValid = false;
    if(name.equals(this.sellerName) && phoneNumber.equals(this.storeTel)) {
      isValid = true;
    }
    return isValid;
  }
}
