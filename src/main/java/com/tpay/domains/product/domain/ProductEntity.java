package com.tpay.domains.product.domain;

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
@Table(name = "product")
@Entity
@ToString
public class ProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "prdNm", length = 100)
  private String name;

  @Column(name = "lneNo", length = 3)
  private String lineNumber;

  @Column(name = "prdCode", length = 5)
  private String code;

  @Column(name = "indPrice", length = 10, unique = true)
  private String price;

  @Builder
  public ProductEntity(String name, String lineNumber, String code, String price) {
    this.name = name;
    this.lineNumber = lineNumber;
    this.code = code;
    this.price = price;
  }
}
