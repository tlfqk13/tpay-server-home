package com.tpay.domains.customer.domain;

import com.tpay.domains.BaseTimeEntity;
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
@Table(name = "customer")
@Entity
@ToString
public class CustomerEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "cusPassNo", length = 63, nullable = false)
  private String passportNumber;

  @NotNull
  @Column(name = "cusNm", length = 40, nullable = false)
  private String customerName;

  @NotNull
  @Column(name = "cusNatn", length = 3, nullable = false)
  private String nation;

  @Builder
  public CustomerEntity(String passportNumber, String customerName, String nation) {
    this.passportNumber = passportNumber;
    this.customerName = customerName;
    this.nation = nation;
  }
}
