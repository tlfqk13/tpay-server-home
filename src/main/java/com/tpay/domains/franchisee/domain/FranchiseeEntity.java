package com.tpay.domains.franchisee.domain;

import com.tpay.domains.BaseTimeEntity;
import com.tpay.domains.franchisee_applicant.domain.FranchiseeApplicantEntity;
import com.tpay.domains.point.domain.SignType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

  @OneToOne
  @JoinColumn(name = "franchisee_applicant_id")
  private FranchiseeApplicantEntity franchiseeApplicantEntity;

  @Builder
  public FranchiseeEntity(
      String businessNumber,
      String storeName,
      String storeAddress,
      String sellerName,
      String storeTel,
      String productCategory,
      String password,
      FranchiseeApplicantEntity franchiseeApplicantEntity) {
    this.memberName = "";
    this.memberNumber = createMemberNumber();
    this.businessNumber = businessNumber.replaceAll("-", "");
    this.storeName = storeName;
    this.storeAddress = storeAddress;
    this.sellerName = sellerName;
    this.storeTel = storeTel;
    this.productCategory = productCategory;
    this.password = password;
    this.balance = 0;
    this.franchiseeApplicantEntity = franchiseeApplicantEntity;
  }

  private String createMemberNumber() {
    return "PAY" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
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
      String storeName, String businessNumber, String storeAddress, String productCategory) {
    this.storeName = storeName;
    this.businessNumber = businessNumber;
    this.storeAddress = storeAddress;
    this.productCategory = productCategory;
  }
}
