package com.tpay.domains.customer.domain;

import com.tpay.domains.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "customer")
@Entity
@ToString
public class CustomerEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "cusPassNo", length = 100)
  private String passportNumber;

  @Column(name = "cusNm", length = 50)
  private String customerName;

  @Column(name = "cusNatn", length = 20)
  private String nation;

  @Builder
  public CustomerEntity(String passportNumber, String customerName, String nation) {
    this.passportNumber = passportNumber;
    this.customerName = customerName;
    this.nation = nation;
  }
}
