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

  @Column(name = "cusPassNo", length = 63, nullable = false)
  private String passportNumber;

  @Column(name = "cusNm", length = 40, nullable = false)
  private String customerName;

  @Column(name = "cusNatn", length = 3, nullable = false)
  private String nation;

  @Builder
  public CustomerEntity(String passportNumber, String customerName, String nation) {
    this.passportNumber = passportNumber;
    this.customerName = customerName;
    this.nation = nation;
  }
}
