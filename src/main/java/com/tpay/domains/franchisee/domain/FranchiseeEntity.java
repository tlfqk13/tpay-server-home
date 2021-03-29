package com.tpay.domains.franchisee.domain;

import com.tpay.domains.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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

  @Column(name = "memNm", length = 50)
  private String memberName;

  @Column(name = "memNo", length = 20)
  private String memberNumber;

  @Column(name = "bizNo", length = 10)
  private String businessNumber;

  @Column(name = "storeNm", length = 50)
  private String storeName;

  @Column(name = "storeAddr", length = 140)
  private String storeAddress;

  @Column(name = "selNm", length = 40)
  private String sellerName;

  @Column(name = "storeTel", length = 26)
  private String storeTel;

  @Column(name = "prdNm", length = 100)
  private String productCategory;

  @Builder
  public FranchiseeEntity(
      String memberName,
      String memberNumber,
      String businessNumber,
      String storeName,
      String storeAddress,
      String sellerName,
      String storeTel,
      String productCategory) {
    this.memberName = memberName;
    this.memberNumber = memberNumber;
    this.businessNumber = businessNumber;
    this.storeName = storeName;
    this.storeAddress = storeAddress;
    this.sellerName = sellerName;
    this.storeTel = storeTel;
    this.productCategory = productCategory;
  }
}
