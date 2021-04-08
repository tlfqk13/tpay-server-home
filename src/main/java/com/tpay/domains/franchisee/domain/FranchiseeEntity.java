package com.tpay.domains.franchisee.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.point.domain.SignType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
  @Column(name = "memNo", length = 20)
  private String memberNumber;

  @NotNull
  @Column(name = "bizNo", length = 10)
  private String businessNumber;

  @NotNull
  @Column(name = "storeNm", length = 50)
  private String storeName;

  @NotNull
  @Column(name = "storeAddr", length = 140)
  private String storeAddress;

  @NotNull
  @Column(name = "selNm", length = 40)
  private String sellerName;

  @NotNull
  @Column(name = "storeTel", length = 26)
  private String storeTel;

  @NotNull
  @Column(name = "prdNm", length = 100)
  private String productCategory;

  @NotNull private String password;

  private long balance;

  @Builder
  public FranchiseeEntity(
      String memberNumber,
      String businessNumber,
      String storeName,
      String storeAddress,
      String sellerName,
      String storeTel,
      String productCategory,
      String password) {
    this.memberNumber = createMemberNumber();
    this.businessNumber = businessNumber;
    this.storeName = storeName;
    this.storeAddress = storeAddress;
    this.sellerName = sellerName;
    this.storeTel = storeTel;
    this.productCategory = productCategory;
    this.password = password;
    this.balance = 0;
  }

  private String createMemberNumber() {
    return "PAY"
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
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
}
